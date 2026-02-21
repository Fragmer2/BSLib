package io.github.fragmer2.bslib.paper.placeholder;

import io.github.fragmer2.bslib.api.placeholder.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PaperPlaceholderRegistry implements PlaceholderRegistry {
    private final Map<String, Function<Player, String>> placeholders = new HashMap<>();

    public PaperPlaceholderRegistry() {
        // Встроенные плейсхолдеры
        register("player", Player::getName);
        register("online", p -> String.valueOf(Bukkit.getOnlinePlayers().size()));
        register("world", p -> p.getWorld().getName());
        register("uuid", p -> p.getUniqueId().toString());
        register("displayname", Player::getDisplayName);
    }

    @Override
    public void register(String key, Function<Player, String> replacer) {
        placeholders.put(key.toLowerCase(), replacer);
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
                        result.append(replacer.apply(player));
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