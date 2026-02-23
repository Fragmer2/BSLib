package io.github.fragmer2.bslib.api.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Clean scheduler API.
 *
 * Usage:
 *   Tasks.sync().run(() -> player.sendMessage("hi"));
 *   Tasks.async().delay(20).run(() -> loadData());
 *   Tasks.sync().delay(10).repeat(20).run(() -> tick());
 *   Tasks.sync().delay(5).run(task -> { if (done) task.cancel(); });
 */
public final class Tasks {
    private static Plugin plugin;
    private static final Set<Integer> trackedTaskIds = ConcurrentHashMap.newKeySet();

    private Tasks() {}

    public static void init(Plugin p) {
        plugin = p;
    }

    public static int trackedTaskCount() {
        cleanupTrackedTasks();
        return trackedTaskIds.size();
    }

    private static void trackTask(BukkitTask task) {
        if (task != null) trackedTaskIds.add(task.getTaskId());
    }

    private static void cleanupTrackedTasks() {
        if (plugin == null) return;
        trackedTaskIds.removeIf(id -> !plugin.getServer().getScheduler().isQueued(id) && !plugin.getServer().getScheduler().isCurrentlyRunning(id));
    }

    public static TaskBuilder sync() {
        return new TaskBuilder(plugin, false);
    }

    public static TaskBuilder async() {
        return new TaskBuilder(plugin, true);
    }

    /**
     * Quick timer: Tasks.timer(20).run(() -> {});
     * Equivalent to Tasks.sync().repeat(interval).run(...)
     */
    public static TaskBuilder timer(long intervalTicks) {
        return new TaskBuilder(plugin, false).repeat(intervalTicks);
    }

    /**
     * Start an async computation chain.
     *
     *   Tasks.compute(() -> heavyDatabaseQuery())
     *        .thenSync(result -> player.sendMessage("Got: " + result));
     *
     *   Tasks.compute(() -> loadFromDB(uuid))
     *        .thenAsync(data -> enrichWithAPI(data))
     *        .thenSync(enriched -> applyToPlayer(player, enriched))
     *        .onError(e -> player.sendMessage("Failed!"));
     */
    public static <T> io.github.fragmer2.bslib.api.thread.AsyncChain<T> compute(java.util.function.Supplier<T> supplier) {
        return new io.github.fragmer2.bslib.api.thread.AsyncChain<T>(supplier);
    }

    public static class TaskBuilder {
        private final Plugin plugin;
        private final boolean async;
        private long delay = 0;
        private long period = -1;

        TaskBuilder(Plugin plugin, boolean async) {
            this.plugin = plugin;
            this.async = async;
        }

        public TaskBuilder delay(long ticks) {
            this.delay = ticks;
            return this;
        }

        public TaskBuilder repeat(long ticks) {
            this.period = ticks;
            return this;
        }

        /**
         * Run a simple task.
         */
        public BukkitTask run(Runnable runnable) {
            if (async) {
                if (period > 0) {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTaskTimerAsynchronously(plugin, runnable, delay, period);
                    trackTask(task);
                    return task;
                } else if (delay > 0) {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTaskLaterAsynchronously(plugin, runnable, delay);
                    trackTask(task);
                    return task;
                } else {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTaskAsynchronously(plugin, runnable);
                    trackTask(task);
                    return task;
                }
            } else {
                if (period > 0) {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTaskTimer(plugin, runnable, delay, period);
                    trackTask(task);
                    return task;
                } else if (delay > 0) {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTaskLater(plugin, runnable, delay);
                    trackTask(task);
                    return task;
                } else {
                    BukkitTask task = plugin.getServer().getScheduler()
                            .runTask(plugin, runnable);
                    trackTask(task);
                    return task;
                }
            }
        }

        /**
         * Run with access to the BukkitTask (for self-cancellation).
         * Usage: Tasks.timer(20).run(task -> { if (done) task.cancel(); });
         *
         * Note: Bukkit's Consumer<BukkitTask> overloads return void,
         * so this method does too.
         */
        public void run(Consumer<BukkitTask> consumer) {
            if (async) {
                if (period > 0) {
                    plugin.getServer().getScheduler()
                            .runTaskTimerAsynchronously(plugin, consumer, delay, period);
                } else if (delay > 0) {
                    plugin.getServer().getScheduler()
                            .runTaskLaterAsynchronously(plugin, consumer, delay);
                } else {
                    plugin.getServer().getScheduler()
                            .runTaskAsynchronously(plugin, consumer);
                }
            } else {
                if (period > 0) {
                    plugin.getServer().getScheduler()
                            .runTaskTimer(plugin, consumer, delay, period);
                } else if (delay > 0) {
                    plugin.getServer().getScheduler()
                            .runTaskLater(plugin, consumer, delay);
                } else {
                    plugin.getServer().getScheduler()
                            .runTask(plugin, consumer);
                }
            }
        }
    }
}
