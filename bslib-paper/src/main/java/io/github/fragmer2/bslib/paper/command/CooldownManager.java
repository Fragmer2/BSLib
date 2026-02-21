package io.github.fragmer2.bslib.paper.command;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
    private final Map<String, Long> cooldowns = new HashMap<>();

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
}