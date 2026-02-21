package io.github.fragmer2.bslib.api.placeholder;

import org.bukkit.entity.Player;

public final class Placeholders {
    private static PlaceholderRegistry registry;

    private Placeholders() {}

    public static void setRegistry(PlaceholderRegistry reg) {
        registry = reg;
    }

    /**
     * Register a placeholder.
     *   Placeholders.register("coins", player -> String.valueOf(getCoins(player)));
     */
    public static void register(String key, java.util.function.Function<org.bukkit.entity.Player, String> replacer) {
        if (registry != null) registry.register(key, replacer);
    }

    public static String apply(Player player, String text) {
        if (registry == null) return text;
        return registry.apply(player, text);
    }
}