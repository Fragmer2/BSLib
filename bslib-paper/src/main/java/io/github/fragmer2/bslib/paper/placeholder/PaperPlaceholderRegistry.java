package io.github.fragmer2.bslib.paper.placeholder;

import io.github.fragmer2.bslib.api.placeholder.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * Paper-specific implementation of the placeholder registry.
 *
 * Provides built-in placeholders for common player and server values,
 * and allows plugins to register custom placeholders at runtime.
 *
 * Built-in placeholders:
 *   {player}      — Player name
 *   {uuid}        — Player UUID
 *   {displayname} — Player display name
 *   {world}       — Current world name
 *   {online}      — Number of online players
 *   {max_players} — Maximum player slots
 *   {health}      — Player health (integer)
 *   {food}        — Player food level
 *   {level}       — Player experience level
 *   {ping}        — Player ping in ms
 *   {gamemode}    — Player game mode
 *
 * Usage in text: "Hello {player}, you are in {world}!"
 */
public class PaperPlaceholderRegistry implements PlaceholderRegistry {
    private final Map<String, Function<Player, String>> placeholders = new ConcurrentHashMap<>();

    public PaperPlaceholderRegistry() {
        // Built-in placeholders
        register("player", Player::getName);
        register("online", p -> String.valueOf(Bukkit.getOnlinePlayers().size()));
        register("max_players", p -> String.valueOf(Bukkit.getMaxPlayers()));
        register("world", p -> p.getWorld().getName());
        register("uuid", p -> p.getUniqueId().toString());
        register("displayname", p -> p.getName()); // Safe fallback; use Adventure for components
        register("health", p -> String.valueOf((int) p.getHealth()));
        register("food", p -> String.valueOf(p.getFoodLevel()));
        register("level", p -> String.valueOf(p.getLevel()));
        register("ping", p -> String.valueOf(p.getPing()));
        register("gamemode", p -> p.getGameMode().name().toLowerCase());
    }

    @Override
    public void register(String key, Function<Player, String> replacer) {
        placeholders.put(key.toLowerCase(), replacer);
    }

    /**
     * Unregister a placeholder by key.
     */
    public void unregister(String key) {
        placeholders.remove(key.toLowerCase());
    }

    /**
     * Check if a placeholder is registered.
     */
    public boolean has(String key) {
        return placeholders.containsKey(key.toLowerCase());
    }

    @Override
    public String apply(Player player, String text) {
        if (text == null || text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        int last = 0;
        int length = text.length();

        for (int i = 0; i < length; i++) {
            if (text.charAt(i) == '{') {
                int end = text.indexOf('}', i);
                if (end != -1) {
                    String key = text.substring(i + 1, end).toLowerCase();
                    Function<Player, String> replacer = placeholders.get(key);
                    if (replacer != null) {
                        result.append(text, last, i);
                        try {
                            String value = replacer.apply(player);
                            result.append(value != null ? value : "");
                        } catch (Exception e) {
                            // Don't let a broken placeholder crash the entire message
                            result.append("{").append(key).append("}");
                            Bukkit.getLogger().log(Level.WARNING,
                                    "Placeholder '{" + key + "}' threw an exception", e);
                        }
                        i = end;
                        last = end + 1;
                    }
                }
            }
        }
        if (last < length) {
            result.append(text, last, length);
        }
        return result.toString();
    }
}
