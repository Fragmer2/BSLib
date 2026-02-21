package io.github.fragmer2.bslib.api.feature;

import io.github.fragmer2.bslib.api.config.Config;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Live feature flags — enable/disable features at runtime.
 *
 * Setup:
 *   Features.init(plugin, "features.yml");
 *   // features.yml:
 *   // new-shop: true
 *   // pvp-arena: false
 *   // beta-gui: true
 *
 * Check:
 *   if (Features.isEnabled("new-shop")) { ... }
 *
 * Toggle at runtime:
 *   Features.enable("pvp-arena");
 *   Features.disable("new-shop");
 *   Features.toggle("beta-gui");
 *
 * Listen for changes:
 *   Features.onChange("new-shop", enabled -> {
 *       if (enabled) startShop(); else stopShop();
 *   });
 *
 * Config changes auto-apply (file watcher).
 */
public final class Features {
    private static Config config;
    private static final Map<String, Boolean> overrides = new ConcurrentHashMap<>();
    private static final Map<String, Consumer<Boolean>> listeners = new ConcurrentHashMap<>();

    private Features() {}

    public static void init(Plugin plugin, String fileName) {
        config = Config.of(plugin, fileName).autoReload(cfg -> {
            plugin.getLogger().info("[Features] Feature flags reloaded");
            // Notify listeners of changes
            for (String key : cfg.keys()) {
                boolean newVal = cfg.node(key).asBool(false);
                Boolean override = overrides.get(key);
                boolean effective = override != null ? override : newVal;
                Consumer<Boolean> listener = listeners.get(key);
                if (listener != null) listener.accept(effective);
            }
        });
    }

    // ========== Check ==========

    public static boolean isEnabled(String feature) {
        Boolean override = overrides.get(feature);
        if (override != null) return override;
        return config != null && config.node(feature).asBool(false);
    }

    public static boolean isDisabled(String feature) {
        return !isEnabled(feature);
    }

    // ========== Runtime toggle ==========

    public static void enable(String feature) {
        set(feature, true);
    }

    public static void disable(String feature) {
        set(feature, false);
    }

    public static void toggle(String feature) {
        set(feature, !isEnabled(feature));
    }

    public static void set(String feature, boolean enabled) {
        overrides.put(feature, enabled);
        Consumer<Boolean> listener = listeners.get(feature);
        if (listener != null) listener.accept(enabled);
    }

    /** Remove runtime override, revert to config value. */
    public static void reset(String feature) {
        overrides.remove(feature);
    }

    /** Remove all runtime overrides. */
    public static void resetAll() {
        overrides.clear();
    }

    // ========== Listen ==========

    public static void onChange(String feature, Consumer<Boolean> listener) {
        listeners.put(feature, listener);
    }

    /** Get all feature keys from config. */
    public static Set<String> all() {
        return config != null ? config.keys() : Set.of();
    }

    public static void clear() {
        overrides.clear();
        listeners.clear();
        if (config != null) config.shutdown();
        config = null;
    }
}
