package io.github.fragmer2.bslib.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Clean config API with dot-notation and auto-reload.
 *
 * Usage:
 *   Config config = Config.of(plugin, "settings.yml");
 *   int hp = config.node("boss.hp").asInt(100);
 *   String name = config.node("boss.name").asString("Dragon");
 *   List<String> worlds = config.node("enabled-worlds").asStringList();
 *   config.node("boss.hp").set(200);
 *   config.save();
 *
 * Auto-reload:
 *   config.autoReload(cfg -> plugin.getLogger().info("Config reloaded!"));
 */
public class Config {
    private final Plugin plugin;
    private final File file;
    private FileConfiguration yaml;
    private Consumer<Config> reloadCallback;
    private WatchService watchService;
    private Thread watchThread;

    private Config(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        load();
    }

    public static Config of(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            // Try to save from resources first
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Cannot create config: " + fileName, e);
                }
            }
        }
        return new Config(plugin, file);
    }

    /**
     * Create config from an arbitrary path (not inside plugin folder).
     */
    public static Config fromFile(Plugin plugin, File file) {
        return new Config(plugin, file);
    }

    // ========== Node access ==========

    public Node node(String path) {
        return new Node(this, path);
    }

    // ========== Load / Save / Reload ==========

    public void load() {
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        load();
        if (reloadCallback != null) {
            reloadCallback.accept(this);
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cannot save config: " + file.getName(), e);
        }
    }

    /**
     * Enable auto-reload when file changes on disk.
     */
    public Config autoReload(Consumer<Config> callback) {
        this.reloadCallback = callback;
        startWatching();
        return this;
    }

    public Config autoReload() {
        return autoReload(null);
    }

    /**
     * Get all top-level keys.
     */
    public Set<String> keys() {
        return yaml.getKeys(false);
    }

    /**
     * Get all keys at a path.
     */
    public Set<String> keys(String path) {
        var section = yaml.getConfigurationSection(path);
        return section != null ? section.getKeys(false) : Set.of();
    }

    /**
     * Check if path exists.
     */
    public boolean has(String path) {
        return yaml.contains(path);
    }

    /**
     * Get raw FileConfiguration for advanced usage.
     */
    public FileConfiguration raw() {
        return yaml;
    }

    public File getFile() {
        return file;
    }

    // ========== File watcher ==========

    private void startWatching() {
        if (watchThread != null) return;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            file.getParentFile().toPath().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            watchThread = new Thread(() -> {
                long lastReload = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        WatchKey key = watchService.take();
                        boolean relevant = false;
                        for (WatchEvent<?> event : key.pollEvents()) {
                            Path changed = (Path) event.context();
                            if (changed.toString().equals(file.getName())) {
                                relevant = true;
                            }
                        }
                        key.reset();
                        if (relevant) {
                            // Debounce: skip if last reload was <500ms ago
                            long now = System.currentTimeMillis();
                            if (now - lastReload < 500) continue;
                            lastReload = now;
                            // Delay to let file finish writing
                            Thread.sleep(200);
                            plugin.getServer().getScheduler().runTask(plugin, this::reload);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "BSLib-ConfigWatcher-" + file.getName());
            watchThread.setDaemon(true);
            watchThread.start();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Cannot start config auto-reload for " + file.getName(), e);
        }
    }

    public void shutdown() {
        if (watchThread != null) {
            watchThread.interrupt();
            watchThread = null;
        }
        if (watchService != null) {
            try { watchService.close(); } catch (IOException ignored) {}
            watchService = null;
        }
    }

    // ========== Node class ==========

    public static class Node {
        private final Config config;
        private final String path;

        Node(Config config, String path) {
            this.config = config;
            this.path = path;
        }

        // --- Getters ---

        public String asString() {
            return config.yaml.getString(path);
        }

        public String asString(String def) {
            return config.yaml.getString(path, def);
        }

        public int asInt() {
            return config.yaml.getInt(path);
        }

        public int asInt(int def) {
            return config.yaml.getInt(path, def);
        }

        public double asDouble() {
            return config.yaml.getDouble(path);
        }

        public double asDouble(double def) {
            return config.yaml.getDouble(path, def);
        }

        public boolean asBool() {
            return config.yaml.getBoolean(path);
        }

        public boolean asBool(boolean def) {
            return config.yaml.getBoolean(path, def);
        }

        public long asLong() {
            return config.yaml.getLong(path);
        }

        public long asLong(long def) {
            return config.yaml.getLong(path, def);
        }

        public List<String> asStringList() {
            return config.yaml.getStringList(path);
        }

        public List<Integer> asIntList() {
            return config.yaml.getIntegerList(path);
        }

        public List<?> asList() {
            return config.yaml.getList(path);
        }

        public Object asObject() {
            return config.yaml.get(path);
        }

        public boolean exists() {
            return config.yaml.contains(path);
        }

        // --- Setter ---

        public void set(Object value) {
            config.yaml.set(path, value);
        }

        // --- Sub-node ---

        public Node child(String sub) {
            return new Node(config, path + "." + sub);
        }

        public Set<String> keys() {
            return config.keys(path);
        }
    }
}
