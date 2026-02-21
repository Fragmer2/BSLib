package io.github.fragmer2.bslib.api.state;

import io.github.fragmer2.bslib.api.reactive.Reactive;
import io.github.fragmer2.bslib.api.task.Tasks;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Manages persistence for a single @State type.
 *
 * Each StateManager handles one type (e.g., PlayerData) and:
 * - Caches loaded instances in memory
 * - Tracks dirty state via Reactive.version()
 * - Saves async, loads async with sync callback
 * - Auto-saves on configurable interval
 */
public class StateManager<T> {
    private final Plugin plugin;
    private final Class<T> type;
    private final State stateAnnotation;
    private final File storageDir;
    private final Map<String, T> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> savedVersions = new ConcurrentHashMap<>();
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "BSLib-State-IO");
        t.setDaemon(true);
        return t;
    });
    private BukkitTask autosaveTask;

    public StateManager(Plugin plugin, Class<T> type) {
        this.plugin = plugin;
        this.type = type;
        this.stateAnnotation = type.getAnnotation(State.class);
        if (stateAnnotation == null) {
            throw new IllegalArgumentException(type.getName() + " is not annotated with @State");
        }
        this.storageDir = new File(plugin.getDataFolder(), "state/" + stateAnnotation.value());
        storageDir.mkdirs();

        // Start autosave if configured
        if (stateAnnotation.autosaveSeconds() > 0) {
            long ticks = stateAnnotation.autosaveSeconds() * 20L;
            autosaveTask = Tasks.async().repeat(ticks).delay(ticks).run(this::saveAllDirty);
        }
    }

    // ========== Get / Load ==========

    /**
     * Get from cache, or create new default instance.
     * Does NOT load from disk — use load() for that.
     */
    public T getOrCreate(String key) {
        return cache.computeIfAbsent(key, k -> {
            T instance = instantiate();
            StateSerializer.setKey(instance, key);
            hookDirtyTracking(key, instance);
            return instance;
        });
    }

    /**
     * Get from cache only. Returns null if not loaded.
     */
    public T get(String key) {
        return cache.get(key);
    }

    /**
     * Load from disk (async) → callback on main thread with result.
     * If already cached, returns cached version immediately.
     */
    public void load(String key, Consumer<T> callback) {
        T cached = cache.get(key);
        if (cached != null) {
            callback.accept(cached);
            return;
        }

        ioExecutor.submit(() -> {
            try {
                Map<String, Object> data = readFromDisk(key);
                Tasks.sync().run(() -> {
                    T instance = instantiate();
                    StateSerializer.setKey(instance, key);
                    if (data != null) {
                        StateSerializer.deserialize(instance, data);
                    }
                    hookDirtyTracking(key, instance);
                    cache.put(key, instance);
                    savedVersions.put(key, computeVersion(instance));
                    callback.accept(instance);
                });
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load state " +
                        stateAnnotation.value() + "/" + key, e);
                Tasks.sync().run(() -> {
                    T instance = getOrCreate(key);
                    callback.accept(instance);
                });
            }
        });
    }

    /**
     * Load from disk (blocking). Use from async context only.
     */
    public T loadSync(String key) {
        T cached = cache.get(key);
        if (cached != null) return cached;

        Map<String, Object> data = readFromDisk(key);
        T instance = instantiate();
        StateSerializer.setKey(instance, key);
        if (data != null) {
            StateSerializer.deserialize(instance, data);
        }
        hookDirtyTracking(key, instance);
        cache.put(key, instance);
        savedVersions.put(key, computeVersion(instance));
        return instance;
    }

    // ========== Save ==========

    /**
     * Save a specific key (async).
     */
    public void save(String key) {
        T instance = cache.get(key);
        if (instance == null) return;

        Map<String, Object> data = StateSerializer.serialize(instance);
        savedVersions.put(key, computeVersion(instance));

        ioExecutor.submit(() -> {
            try {
                writeToDisk(key, data);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to save state " +
                        stateAnnotation.value() + "/" + key, e);
            }
        });
    }

    /**
     * Save a specific key (blocking). Use from async context.
     */
    public void saveSync(String key) {
        T instance = cache.get(key);
        if (instance == null) return;

        Map<String, Object> data = StateSerializer.serialize(instance);
        savedVersions.put(key, computeVersion(instance));
        writeToDisk(key, data);
    }

    /**
     * Save all dirty (changed since last save) instances.
     */
    public void saveAllDirty() {
        for (Map.Entry<String, T> entry : cache.entrySet()) {
            String key = entry.getKey();
            T instance = entry.getValue();
            long currentVersion = computeVersion(instance);
            Long lastSaved = savedVersions.get(key);
            if (lastSaved == null || currentVersion != lastSaved) {
                Map<String, Object> data = StateSerializer.serialize(instance);
                savedVersions.put(key, currentVersion);
                try {
                    writeToDisk(key, data);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Autosave failed: " +
                            stateAnnotation.value() + "/" + key, e);
                }
            }
        }
    }

    /**
     * Save ALL cached instances (for shutdown).
     */
    public void saveAll() {
        for (Map.Entry<String, T> entry : cache.entrySet()) {
            try {
                Map<String, Object> data = StateSerializer.serialize(entry.getValue());
                writeToDisk(entry.getKey(), data);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Save failed: " +
                        stateAnnotation.value() + "/" + entry.getKey(), e);
            }
        }
    }

    // ========== Cache management ==========

    /**
     * Remove from cache (but keep on disk). Call when player quits.
     */
    public void unload(String key) {
        save(key); // save before unloading
        cache.remove(key);
        savedVersions.remove(key);
    }

    /**
     * Remove from cache and delete from disk.
     */
    public void delete(String key) {
        cache.remove(key);
        savedVersions.remove(key);
        File file = getFile(key);
        if (file.exists()) file.delete();
    }

    /** Check if key exists on disk. */
    public boolean exists(String key) {
        return cache.containsKey(key) || getFile(key).exists();
    }

    /** Get all cached keys. */
    public Set<String> cachedKeys() {
        return Collections.unmodifiableSet(cache.keySet());
    }

    /** Get all keys on disk. */
    public Set<String> allKeys() {
        Set<String> keys = new HashSet<>(cache.keySet());
        File[] files = storageDir.listFiles();
        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                if (name.endsWith(".yml")) keys.add(name.replace(".yml", ""));
                if (name.endsWith(".json")) keys.add(name.replace(".json", ""));
            }
        }
        return keys;
    }

    // ========== Shutdown ==========

    public void shutdown() {
        if (autosaveTask != null) autosaveTask.cancel();
        saveAll();
        ioExecutor.shutdown();
        try {
            ioExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}
        cache.clear();
        savedVersions.clear();
    }

    // ========== Internal: Disk I/O ==========

    private File getFile(String key) {
        String ext = stateAnnotation.backend() == State.Backend.JSON ? ".json" : ".yml";
        return new File(storageDir, key + ext);
    }

    private Map<String, Object> readFromDisk(String key) {
        File file = getFile(key);
        if (!file.exists()) return null;

        if (stateAnnotation.backend() == State.Backend.YAML) {
            return readYaml(file);
        } else {
            return readJson(file);
        }
    }

    private void writeToDisk(String key, Map<String, Object> data) {
        File file = getFile(key);
        storageDir.mkdirs();

        if (stateAnnotation.backend() == State.Backend.YAML) {
            writeYaml(file, data);
        } else {
            writeJson(file, data);
        }
    }

    private Map<String, Object> readYaml(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : yaml.getKeys(false)) {
            map.put(key, yaml.get(key));
        }
        return map;
    }

    private void writeYaml(File file, Map<String, Object> data) {
        YamlConfiguration yaml = new YamlConfiguration();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            yaml.set(entry.getKey(), entry.getValue());
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write YAML: " + file, e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readJson(File file) {
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            // Minimal JSON parser — supports flat key-value maps
            // For production, use Gson. This handles basic types.
            return parseSimpleJson(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON: " + file, e);
        }
    }

    private void writeJson(File file, Map<String, Object> data) {
        try (Writer w = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            w.write("{\n");
            Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                w.write("  \"" + entry.getKey() + "\": " + toJsonValue(entry.getValue()));
                if (it.hasNext()) w.write(",");
                w.write("\n");
            }
            w.write("}\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON: " + file, e);
        }
    }

    private String toJsonValue(Object val) {
        if (val == null) return "null";
        if (val instanceof String s) return "\"" + s.replace("\"", "\\\"") + "\"";
        if (val instanceof Number || val instanceof Boolean) return val.toString();
        if (val instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(toJsonValue(list.get(i)));
            }
            return sb.append("]").toString();
        }
        if (val instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            Iterator<?> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) it.next();
                sb.append("\"").append(e.getKey()).append("\": ").append(toJsonValue(e.getValue()));
                if (it.hasNext()) sb.append(", ");
            }
            return sb.append("}").toString();
        }
        return "\"" + val + "\"";
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseSimpleJson(String json) {
        // Delegate to Bukkit's YAML parser (handles JSON subset)
        // This is a pragmatic choice — works for flat structures
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            // YAML is a superset of JSON
            yaml.loadFromString(json);
            Map<String, Object> map = new LinkedHashMap<>();
            for (String key : yaml.getKeys(false)) {
                map.put(key, yaml.get(key));
            }
            return map;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    // ========== Internal: Dirty tracking ==========

    /**
     * Compute a version hash from all Reactive fields.
     */
    private long computeVersion(T instance) {
        long version = 0;
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object val = field.get(instance);
                if (val instanceof Reactive<?> r) {
                    version += r.version();
                }
            } catch (Exception ignored) {}
        }
        return version;
    }

    /**
     * Hook Reactive fields to mark cache entry as potentially dirty.
     * This is lightweight — actual dirty check happens on save via version comparison.
     */
    private void hookDirtyTracking(String key, T instance) {
        // No-op: dirty tracking uses version comparison (computeVersion)
        // This avoids listener overhead on every Reactive.set()
    }

    private T instantiate() {
        try {
            var ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate @State class: " + type.getName() +
                    ". Ensure it has a no-arg constructor.", e);
        }
    }
}
