package io.github.fragmer2.bslib.paper.command;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Manages command-specific cooldowns for the Paper command framework.
 *
 * Each cooldown is identified by a composite key of player UUID + command key.
 * Expired cooldowns are lazily cleaned up on access, and can also be
 * bulk-cleaned via {@link #cleanup()} or {@link #clearPlayer(Player)}.
 */
public class CooldownManager {
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    public void setCooldown(Player player, String key, long duration, TimeUnit unit) {
        String id = player.getUniqueId() + ":" + key;
        long expiry = System.currentTimeMillis() + unit.toMillis(duration);
        cooldowns.put(id, expiry);
    }

    public boolean hasCooldown(Player player, String key) {
        String id = player.getUniqueId() + ":" + key;
        Long expiry = cooldowns.get(id);
        if (expiry == null) return false;
        if (expiry < System.currentTimeMillis()) {
            cooldowns.remove(id);
            return false;
        }
        return true;
    }

    public long getRemaining(Player player, String key, TimeUnit unit) {
        String id = player.getUniqueId() + ":" + key;
        Long expiry = cooldowns.get(id);
        if (expiry == null) return 0;
        long remaining = expiry - System.currentTimeMillis();
        if (remaining <= 0) {
            cooldowns.remove(id);
            return 0;
        }
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }

    /** Remove all cooldowns for a specific player. */
    public void clearPlayer(Player player) {
        String prefix = player.getUniqueId() + ":";
        cooldowns.keySet().removeIf(k -> k.startsWith(prefix));
    }

    /** Remove all expired cooldown entries from memory. */
    public void cleanup() {
        long now = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(e -> e.getValue() < now);
    }

    /** Clear all cooldowns. */
    public void clear() {
        cooldowns.clear();
    }
}
