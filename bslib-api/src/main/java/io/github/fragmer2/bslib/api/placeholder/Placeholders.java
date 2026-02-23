package io.github.fragmer2.bslib.api.placeholder;

import io.github.fragmer2.bslib.api.reactive.Reactive;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Placeholders {
    /** External registry (e.g. PlaceholderAPI bridge). */
    private static PlaceholderRegistry registry;

    /** Built-in fallback registry — always available, no setup required. */
    private static final DefaultRegistry defaultRegistry = new DefaultRegistry();

    private Placeholders() {}

    public static void setRegistry(PlaceholderRegistry reg) {
        registry = reg;
    }

    /**
     * Register a placeholder.
     * Works even without an external PlaceholderAPI registry.
     *
     *   Placeholders.register("coins", player -> String.valueOf(getCoins(player)));
     */
    public static void register(String key, Function<Player, String> replacer) {
        defaultRegistry.register(key, replacer);
        if (registry != null) registry.register(key, replacer);
    }

    /**
     * Register a static (non-player) placeholder.
     *
     *   Placeholders.register("server_name", () -> Bukkit.getServer().getName());
     */
    public static void register(String key, Supplier<String> supplier) {
        defaultRegistry.register(key, player -> supplier.get());
        if (registry != null) registry.register(key, player -> supplier.get());
    }

    /**
     * Bind a Reactive value as a placeholder.
     * The placeholder will always return the current value of the Reactive.
     *
     *   Reactive<Integer> coins = Reactive.of(0);
     *   Placeholders.asPlaceholder("coins", coins);
     *   // Now {coins} in any message will show the current value
     */
    public static <T> void asPlaceholder(String key, Reactive<T> reactive) {
        register(key, player -> String.valueOf(reactive.get()));
    }

    /**
     * Bind a per-player Reactive value as a placeholder.
     * The supplier is called per player to get their Reactive instance.
     *
     *   Placeholders.asPlaceholder("coins", player -> States.of(player, PlayerData.class).coins);
     */
    public static <T> void asPlaceholder(String key, Function<Player, Reactive<T>> reactiveSupplier) {
        register(key, player -> String.valueOf(reactiveSupplier.apply(player).get()));
    }

    /**
     * Apply all registered placeholders to a text string.
     * Uses external registry first, then falls back to built-in registry.
     */
    public static String apply(Player player, String text) {
        if (text == null) return null;
        // Apply built-in placeholders first
        String result = defaultRegistry.apply(player, text);
        // Then apply external registry (e.g. PlaceholderAPI) if available
        if (registry != null) result = registry.apply(player, result);
        return result;
    }

    /**
     * Apply placeholders without a player context (static placeholders only).
     */
    public static String apply(String text) {
        return apply(null, text);
    }

    // ========== Built-in Default Registry ==========

    /**
     * Simple built-in registry that is always available.
     * Supports {key} format placeholders.
     */
    static final class DefaultRegistry implements PlaceholderRegistry {
        private final Map<String, Function<Player, String>> placeholders = new ConcurrentHashMap<>();

        @Override
        public void register(String key, Function<Player, String> replacer) {
            placeholders.put(key, replacer);
        }

        @Override
        public String apply(Player player, String text) {
            if (text == null || text.isEmpty() || placeholders.isEmpty()) return text;
            for (Map.Entry<String, Function<Player, String>> entry : placeholders.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                if (text.contains(placeholder)) {
                    try {
                        String value = entry.getValue().apply(player);
                        text = text.replace(placeholder, value != null ? value : "");
                    } catch (Exception ignored) {}
                }
            }
            return text;
        }
    }
}