package io.github.fragmer2.bslib.api.debug;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Developer debug mode.
 *
 * /framework debug  → shows full system report
 *
 * Programmatic:
 *   Debug.enable();
 *   Debug.trackTime("shop-update", () -> updateShop());
 *   Debug.report(sender);
 */
public final class Debug {
    private static boolean enabled = false;
    private static final Map<String, TimingData> timings = new ConcurrentHashMap<>();

    private Debug() {}

    public static void enable() { enabled = true; }
    public static void disable() { enabled = false; }
    public static boolean isEnabled() { return enabled; }
    public static void toggle() { enabled = !enabled; }

    // ========== Timing ==========

    /**
     * Track execution time of a task.
     *   Debug.trackTime("shop-update", () -> updateShop());
     */
    public static void trackTime(String label, Runnable task) {
        if (!enabled) {
            task.run();
            return;
        }
        long start = System.nanoTime();
        task.run();
        long elapsed = System.nanoTime() - start;

        timings.computeIfAbsent(label, k -> new TimingData()).record(elapsed);
    }

    /**
     * Manual timing start.
     */
    public static long startTiming() {
        return System.nanoTime();
    }

    /**
     * Record timing end.
     */
    public static void endTiming(String label, long startNano) {
        if (!enabled) return;
        long elapsed = System.nanoTime() - startNano;
        timings.computeIfAbsent(label, k -> new TimingData()).record(elapsed);
    }

    // ========== Report ==========

    /**
     * Send debug report to a command sender.
     */
    public static void report(CommandSender sender) {
        sender.sendMessage("§6§l=== BSLib Debug Report ===");
        sender.sendMessage("");

        // Memory
        Runtime rt = Runtime.getRuntime();
        long usedMb = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        long maxMb = rt.maxMemory() / (1024 * 1024);
        sender.sendMessage("§e⚡ Memory: §f" + usedMb + "MB / " + maxMb + "MB §7(" +
                (usedMb * 100 / maxMb) + "% used)");

        // TPS
        double[] tps = Bukkit.getTPS();
        String tpsColor = tps[0] >= 19.5 ? "§a" : tps[0] >= 17 ? "§e" : "§c";
        sender.sendMessage("§e⚡ TPS: " + tpsColor + String.format("%.1f", tps[0]) +
                " §7(5m: " + String.format("%.1f", tps[1]) +
                ", 15m: " + String.format("%.1f", tps[2]) + ")");

        // Players
        sender.sendMessage("§e⚡ Players: §f" + Bukkit.getOnlinePlayers().size() +
                "/" + Bukkit.getMaxPlayers());

        // Plugins
        sender.sendMessage("§e⚡ Plugins: §f" + Bukkit.getPluginManager().getPlugins().length);

        // Listener count
        int totalListeners = 0;
        for (HandlerList hl : HandlerList.getHandlerLists()) {
            totalListeners += hl.getRegisteredListeners().length;
        }
        sender.sendMessage("§e⚡ Registered listeners: §f" + totalListeners);

        // Worlds
        sender.sendMessage("§e⚡ Worlds: §f" + Bukkit.getWorlds().size() +
                " §7(entities: " + Bukkit.getWorlds().stream()
                .mapToInt(w -> w.getEntities().size()).sum() + ")");

        // Timings
        if (!timings.isEmpty()) {
            sender.sendMessage("");
            sender.sendMessage("§6§lTimings:");
            timings.forEach((label, data) -> {
                double avgMs = data.avgNanos() / 1_000_000.0;
                String color = avgMs < 1 ? "§a" : avgMs < 5 ? "§e" : "§c";
                sender.sendMessage("  §7" + label + ": " + color +
                        String.format("%.2fms", avgMs) +
                        " §7(calls: " + data.count.get() + ", max: " +
                        String.format("%.2fms", data.maxNanos.get() / 1_000_000.0) + ")");
            });
        }

        // Thread info
        sender.sendMessage("");
        sender.sendMessage("§6§lThreads:");
        sender.sendMessage("  §7Main thread: §f" + (Bukkit.isPrimaryThread() ? "§aYes" : "§cNo"));
        sender.sendMessage("  §7Active threads: §f" + Thread.activeCount());
    }

    /** Reset all timing data. */
    public static void resetTimings() {
        timings.clear();
    }

    // ========== Internal ==========

    private static class TimingData {
        final AtomicLong totalNanos = new AtomicLong();
        final AtomicLong count = new AtomicLong();
        final AtomicLong maxNanos = new AtomicLong();

        void record(long nanos) {
            totalNanos.addAndGet(nanos);
            count.incrementAndGet();
            maxNanos.updateAndGet(current -> Math.max(current, nanos));
        }

        double avgNanos() {
            long c = count.get();
            return c > 0 ? (double) totalNanos.get() / c : 0;
        }
    }
}
