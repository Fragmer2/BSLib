package io.github.fragmer2.bslib.api.state;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Static facade for the State Persistence Engine.
 *
 * ===== Register a state type: =====
 *   States.register(plugin, PlayerData.class);
 *
 * ===== Use with players: =====
 *   PlayerData data = States.of(player, PlayerData.class);
 *   data.coins.set(500);  // auto-saved, reactive, everything works
 *
 * ===== Load with callback (async): =====
 *   States.load(player, PlayerData.class, data -> {
 *       // data is loaded from disk, now on main thread
 *       data.coins.set(100);
 *   });
 *
 * ===== Manual save: =====
 *   States.save(player, PlayerData.class);
 *
 * ===== Non-player states: =====
 *   States.of("server-config", ServerData.class);
 *
 * The system handles:
 * ✅ Async disk I/O
 * ✅ In-memory cache
 * ✅ Dirty tracking (only saves when changed)
 * ✅ Autosave on interval
 * ✅ Auto-unload on player quit
 * ✅ Reactive field synchronization
 */
public final class States {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<?>, StateManager> managers = new ConcurrentHashMap<>();

    /** Tracks which plugin registered each state type (for plugin-scoped shutdown). */
    private static final Map<Class<?>, Plugin> pluginOwners = new ConcurrentHashMap<>();

    private States() {}

    // ========== Registration ==========

    /**
     * Register a @State class. Call once per type (usually in enable()).
     * Or let @AutoScan detect them automatically.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> void register(Plugin plugin, Class<T> type) {
        if (!type.isAnnotationPresent(State.class)) {
            throw new IllegalArgumentException(type.getName() + " is not annotated with @State");
        }
        managers.put(type, new StateManager<>(plugin, type));
        pluginOwners.put(type, plugin);
    }

    /**
     * Auto-register a @State class if not already registered.
     * Convenience method — safe to call multiple times.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> void autoRegister(Plugin plugin, Class<T> type) {
        if (!managers.containsKey(type)) {
            register(plugin, type);
        }
    }

    // ========== Global (non-player) state shortcuts ==========

    /**
     * Get or create a global (non-player) state instance.
     * Matches documentation: States.get(plugin, MyData.class).
     * Auto-registers the type if not yet registered.
     *
     * Usage:
     *   BossData data = States.get(plugin, BossData.class);
     *   data.bossHealth.set(1000);
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Plugin plugin, Class<T> type) {
        autoRegister(plugin, type);
        String key = "__global__:" + type.getSimpleName();
        return (T) manager(type).getOrCreate(key);
    }

    /**
     * Save a global state instance.
     */
    public static <T> void saveGlobal(Plugin plugin, Class<T> type) {
        autoRegister(plugin, type);
        manager(type).save("__global__:" + type.getSimpleName());
    }

    /**
     * Load a global state instance from disk (async) → callback on main thread.
     */
    @SuppressWarnings("unchecked")
    public static <T> void loadGlobal(Plugin plugin, Class<T> type, Consumer<T> callback) {
        autoRegister(plugin, type);
        manager(type).load("__global__:" + type.getSimpleName(), (Consumer) callback);
    }

    // ========== Player shortcuts ==========

    /**
     * Get or create state for a player (from cache or new default).
     * Instant — no disk I/O. If data hasn't been loaded yet, returns defaults.
     */
    @SuppressWarnings("unchecked")
    public static <T> T of(Player player, Class<T> type) {
        return (T) manager(type).getOrCreate(player.getUniqueId().toString());
    }

    /**
     * Alias for of(player, type) — matches documentation naming getOrCreate().
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrCreate(Player player, Class<T> type) {
        return of(player, type);
    }

    /**
     * Load state for a player from disk (async) → callback on main thread.
     * Best practice: call in Sessions.onJoin().
     */
    @SuppressWarnings("unchecked")
    public static <T> void load(Player player, Class<T> type, Consumer<T> callback) {
        manager(type).load(player.getUniqueId().toString(), (Consumer) callback);
    }

    /**
     * Save state for a player (async).
     */
    public static <T> void save(Player player, Class<T> type) {
        manager(type).save(player.getUniqueId().toString());
    }

    /**
     * Unload (save + remove from cache). Call on player quit.
     */
    public static <T> void unload(Player player, Class<T> type) {
        manager(type).unload(player.getUniqueId().toString());
    }

    // ========== Generic key access ==========

    /**
     * Get or create state by string key.
     */
    @SuppressWarnings("unchecked")
    public static <T> T of(String key, Class<T> type) {
        return (T) manager(type).getOrCreate(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> void load(String key, Class<T> type, Consumer<T> callback) {
        manager(type).load(key, (Consumer) callback);
    }

    public static <T> void save(String key, Class<T> type) {
        manager(type).save(key);
    }

    public static <T> void unload(String key, Class<T> type) {
        manager(type).unload(key);
    }

    public static <T> void delete(String key, Class<T> type) {
        manager(type).delete(key);
    }

    public static <T> boolean exists(String key, Class<T> type) {
        return manager(type).exists(key);
    }

    // ========== Bulk operations ==========

    /**
     * Save all dirty instances of a type.
     */
    public static <T> void saveAllDirty(Class<T> type) {
        manager(type).saveAllDirty();
    }

    /**
     * Save ALL cached instances of a type.
     */
    public static <T> void saveAll(Class<T> type) {
        manager(type).saveAll();
    }

    /**
     * Unload all cached states for a player (all types).
     * Call this on player quit.
     */
    @SuppressWarnings("rawtypes")
    public static void unloadAll(Player player) {
        String key = player.getUniqueId().toString();
        for (StateManager manager : managers.values()) {
            if (manager.get(key) != null) {
                manager.unload(key);
            }
        }
    }

    // ========== Lifecycle ==========

    /**
     * Shutdown all state managers. Called on plugin disable.
     */
    @SuppressWarnings("rawtypes")
    public static void shutdown() {
        for (StateManager manager : managers.values()) {
            manager.shutdown();
        }
        managers.clear();
    }

    /**
     * Shutdown managers for a specific plugin only.
     * Only shuts down state types registered by this plugin.
     */
    @SuppressWarnings("rawtypes")
    public static void shutdown(Plugin plugin) {
        Iterator<Map.Entry<Class<?>, StateManager>> it = managers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Class<?>, StateManager> entry = it.next();
            Plugin owner = pluginOwners.get(entry.getKey());
            if (owner == plugin) {
                entry.getValue().shutdown();
                pluginOwners.remove(entry.getKey());
                it.remove();
            }
        }
    }

    // ========== Internal ==========

    @SuppressWarnings("rawtypes")
    private static StateManager manager(Class<?> type) {
        StateManager manager = managers.get(type);
        if (manager == null) {
            throw new IllegalStateException("State type not registered: " + type.getName() +
                    ". Call States.register(plugin, " + type.getSimpleName() + ".class) first.");
        }
        return manager;
    }

    /** Get all registered state types (for debug). */
    public static Set<Class<?>> registeredTypes() {
        return Collections.unmodifiableSet(managers.keySet());
    }
}
