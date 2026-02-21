package io.github.fragmer2.bslib.api.cooldown;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Universal cooldown system — shared between GUI, commands, interactions.
 *
 * Check and set:
 *   if (Cooldowns.of(player).key("shop.buy").seconds(5).check()) {
 *       // not on cooldown — action proceeds, cooldown applied
 *   } else {
 *       player.sendMessage("Wait " + Cooldowns.of(player).key("shop.buy").remaining() + "s");
 *   }
 *
 * Simple check:
 *   Cooldowns.of(player).key("heal").seconds(10).bypass("admin.heal").check();
 *
 * Global cooldowns (not per-player):
 *   Cooldowns.global().key("world-event").minutes(30).check();
 *
 * Manual control:
 *   Cooldowns.of(player).key("pvp").set(30, TimeUnit.SECONDS);
 *   Cooldowns.of(player).key("pvp").clear();
 */
public final class Cooldowns {
    private static final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    private Cooldowns() {}

    /** Player-scoped cooldown builder. */
    public static CooldownBuilder of(Player player) {
        return new CooldownBuilder(player.getUniqueId().toString(), player);
    }

    /** Global (non-player) cooldown builder. */
    public static CooldownBuilder global() {
        return new CooldownBuilder("__global__", null);
    }

    /** Direct check — is this key on cooldown? */
    public static boolean isOnCooldown(Player player, String key) {
        return isOnCooldown(player.getUniqueId().toString(), key);
    }

    private static boolean isOnCooldown(String scope, String key) {
        Long expiry = cooldowns.get(scope + ":" + key);
        if (expiry == null) return false;
        if (expiry < System.currentTimeMillis()) {
            cooldowns.remove(scope + ":" + key);
            return false;
        }
        return true;
    }

    /** Clear all cooldowns. */
    public static void clear() {
        cooldowns.clear();
    }

    // ========== Builder ==========

    public static class CooldownBuilder {
        private final String scope;
        private final Player player; // FIX: store player for bypass check
        private String key;
        private long durationMs = 0;
        private String bypassPermission;

        CooldownBuilder(String scope, Player player) {
            this.scope = scope;
            this.player = player;
        }

        public CooldownBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CooldownBuilder seconds(int seconds) {
            this.durationMs = seconds * 1000L;
            return this;
        }

        public CooldownBuilder minutes(int minutes) {
            this.durationMs = minutes * 60_000L;
            return this;
        }

        public CooldownBuilder duration(long amount, TimeUnit unit) {
            this.durationMs = unit.toMillis(amount);
            return this;
        }

        public CooldownBuilder bypass(String permission) {
            this.bypassPermission = permission;
            return this;
        }

        /**
         * Check if NOT on cooldown. If not, applies the cooldown.
         * Returns true if action should proceed (not on cooldown).
         * Returns false if on cooldown (action should be blocked).
         * Uses the player from of(player) for bypass permission check.
         */
        public boolean check() {
            return check(this.player);
        }

        /**
         * Check with explicit player (for bypass permission).
         */
        public boolean check(Player player) {
            if (key == null) throw new IllegalStateException("Cooldown key not set! Call .key() before .check()");

            if (player != null && bypassPermission != null && !bypassPermission.isEmpty()
                    && player.hasPermission(bypassPermission)) {
                return true;
            }
            String fullKey = scope + ":" + key;
            Long expiry = cooldowns.get(fullKey);
            if (expiry != null && expiry > System.currentTimeMillis()) {
                return false; // on cooldown
            }
            // Apply cooldown
            if (durationMs > 0) {
                cooldowns.put(fullKey, System.currentTimeMillis() + durationMs);
            }
            return true;
        }

        /**
         * Get remaining cooldown time in seconds.
         */
        public long remaining() {
            return remaining(TimeUnit.SECONDS);
        }

        public long remaining(TimeUnit unit) {
            if (key == null) return 0;
            String fullKey = scope + ":" + key;
            Long expiry = cooldowns.get(fullKey);
            if (expiry == null) return 0;
            long rem = expiry - System.currentTimeMillis();
            if (rem <= 0) {
                cooldowns.remove(fullKey);
                return 0;
            }
            return unit.convert(rem, TimeUnit.MILLISECONDS);
        }

        /** Manually set a cooldown. */
        public void set(long amount, TimeUnit unit) {
            if (key == null) throw new IllegalStateException("Cooldown key not set!");
            cooldowns.put(scope + ":" + key, System.currentTimeMillis() + unit.toMillis(amount));
        }

        /** Clear this specific cooldown. */
        public void clear() {
            if (key != null) cooldowns.remove(scope + ":" + key);
        }
    }
}
