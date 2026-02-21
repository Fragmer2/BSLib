package io.github.fragmer2.bslib.api.debug;

import io.github.fragmer2.bslib.api.button.Button;
import io.github.fragmer2.bslib.api.menu.Menu;
import io.github.fragmer2.bslib.api.menu.MenuView;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GUI Inspector — live debugging for menu developers.
 *
 * Toggle:
 *   /bslib dev           → toggle dev mode for the player
 *   GuiInspector.toggle(player);
 *
 * When dev mode is ON:
 *   - Clicking any GUI slot shows debug info instead of executing
 *   - Shows: menu class, slot, button type, reactive status, render time
 *
 * Programmatic:
 *   GuiInspector.enable(player);
 *   GuiInspector.disable(player);
 *   GuiInspector.isEnabled(player);
 */
public final class GuiInspector {
    private static final Set<UUID> devPlayers = ConcurrentHashMap.newKeySet();
    private static final Map<String, RenderStats> renderStats = new ConcurrentHashMap<>();

    private GuiInspector() {}

    // ========== Dev Mode ==========

    public static void enable(Player player) {
        devPlayers.add(player.getUniqueId());
        player.sendMessage("§6[BSLib] §aDev mode ON §7— click any GUI slot to inspect");
    }

    public static void disable(Player player) {
        devPlayers.remove(player.getUniqueId());
        player.sendMessage("§6[BSLib] §cDev mode OFF");
    }

    public static void toggle(Player player) {
        if (isEnabled(player)) {
            disable(player);
        } else {
            enable(player);
        }
    }

    public static boolean isEnabled(Player player) {
        return devPlayers.contains(player.getUniqueId());
    }

    // ========== Inspection ==========

    /**
     * Called by GuiManager when a dev-mode player clicks a slot.
     * Returns true if the click was intercepted (don't run normal handler).
     */
    public static boolean inspect(Player player, Menu menu, int slot, Button button, MenuView view) {
        if (!isEnabled(player)) return false;

        player.sendMessage("§6§l═══ GUI Inspector ═══");
        player.sendMessage("");

        // Menu info
        player.sendMessage("§e📋 Menu: §f" + menu.getClass().getSimpleName());
        player.sendMessage("§e📐 Size: §f" + menu.getRows() + " rows (" + (menu.getRows() * 9) + " slots)");
        player.sendMessage("§e🔢 Slot: §f" + slot + " §7(row " + (slot / 9) + ", col " + (slot % 9) + ")");
        player.sendMessage("§e🔘 Buttons: §f" + menu.getButtons().size() + " registered");

        if (button == null) {
            player.sendMessage("§e🔲 Button: §7(empty slot)");
        } else {
            // Button info
            player.sendMessage("");
            String type = button.isDynamic() ? "§aDynamic" : "§7Static";
            player.sendMessage("§e🎯 Type: " + type);
            player.sendMessage("§e⚡ Reactive: " + (button.isReactive() ? "§aYes (auto-updates)" : "§7No"));
            player.sendMessage("§e🚫 Cancel click: " + (button.shouldCancel() ? "§aYes" : "§cNo"));

            // Render timing
            long start = System.nanoTime();
            try {
                button.render(view);
            } catch (Exception e) {
                player.sendMessage("§e⏱ Render: §c ERROR: " + e.getMessage());
            }
            double renderMs = (System.nanoTime() - start) / 1_000_000.0;
            String renderColor = renderMs < 0.5 ? "§a" : renderMs < 2 ? "§e" : "§c";
            player.sendMessage("§e⏱ Render time: " + renderColor + String.format("%.3fms", renderMs));

            // Stats
            String statsKey = menu.getClass().getSimpleName() + ":" + slot;
            RenderStats stats = renderStats.get(statsKey);
            if (stats != null) {
                player.sendMessage("§e📊 Total renders: §f" + stats.renderCount);
                player.sendMessage("§e📊 Updates/sec: §f" + String.format("%.1f", stats.updatesPerSecond()));
                double avgMs = stats.avgRenderNanos() / 1_000_000.0;
                player.sendMessage("§e📊 Avg render: §f" + String.format("%.3fms", avgMs));
            }
        }

        // Policies
        if (!menu.getPolicies().isEmpty()) {
            player.sendMessage("");
            player.sendMessage("§e🔒 Policies: §f" + menu.getPolicies());
        }

        player.sendMessage("");
        player.sendMessage("§6§l═══════════════════");

        return true; // intercepted — don't run normal click handler
    }

    // ========== Render Stats Tracking ==========

    /**
     * Record a render event (called by GuiManager refresh loop).
     */
    public static void recordRender(String menuClass, int slot, long nanos) {
        if (devPlayers.isEmpty()) return; // skip if no one is in dev mode
        String key = menuClass + ":" + slot;
        renderStats.computeIfAbsent(key, k -> new RenderStats()).record(nanos);
    }

    public static void clearStats() {
        renderStats.clear();
    }

    public static void clearPlayer(Player player) {
        devPlayers.remove(player.getUniqueId());
    }

    // ========== Stats ==========

    private static class RenderStats {
        long renderCount = 0;
        long totalNanos = 0;
        long firstRenderTime = 0;

        void record(long nanos) {
            if (firstRenderTime == 0) firstRenderTime = System.currentTimeMillis();
            renderCount++;
            totalNanos += nanos;
        }

        double avgRenderNanos() {
            return renderCount > 0 ? (double) totalNanos / renderCount : 0;
        }

        double updatesPerSecond() {
            long elapsed = System.currentTimeMillis() - firstRenderTime;
            if (elapsed < 1000) return renderCount;
            return (double) renderCount / (elapsed / 1000.0);
        }
    }
}
