package io.github.fragmer2.bslib.api.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Proxy;
import java.util.Objects;
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

    private static void untrackTask(BukkitTask task) {
        if (task != null) trackedTaskIds.remove(task.getTaskId());
    }


    private static BukkitTask wrapTrackedTask(BukkitTask delegate) {
        if (delegate == null) return null;
        return (BukkitTask) Proxy.newProxyInstance(
                BukkitTask.class.getClassLoader(),
                new Class[]{BukkitTask.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("cancel".equals(name)) {
                        try {
                            return method.invoke(delegate, args);
                        } finally {
                            untrackTask(delegate);
                        }
                    }
                    if ("equals".equals(name) && args != null && args.length == 1) {
                        return proxy == args[0];
                    }
                    if ("hashCode".equals(name) && (args == null || args.length == 0)) {
                        return System.identityHashCode(proxy);
                    }
                    if ("toString".equals(name) && (args == null || args.length == 0)) {
                        return "TrackedTask(" + delegate + ")";
                    }
                    return method.invoke(delegate, args);
                }
        );
    }

    private static Runnable wrapRunnable(Runnable runnable, boolean repeating) {
        Objects.requireNonNull(runnable, "runnable");
        if (repeating) {
            return runnable;
        }
        return () -> {
            try {
                runnable.run();
            } finally {
                BukkitTask current = plugin.getServer().getScheduler().getCurrentTask();
                if (current != null) {
                    untrackTask(current);
                }
            }
        };
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
            boolean repeating = period > 0;
            Runnable wrapped = wrapRunnable(runnable, repeating);
            BukkitTask task;
            if (async) {
                if (repeating) {
                    task = plugin.getServer().getScheduler()
                            .runTaskTimerAsynchronously(plugin, wrapped, delay, period);
                } else if (delay > 0) {
                    task = plugin.getServer().getScheduler()
                            .runTaskLaterAsynchronously(plugin, wrapped, delay);
                } else {
                    task = plugin.getServer().getScheduler()
                            .runTaskAsynchronously(plugin, wrapped);
                }
            } else {
                if (repeating) {
                    task = plugin.getServer().getScheduler()
                            .runTaskTimer(plugin, wrapped, delay, period);
                } else if (delay > 0) {
                    task = plugin.getServer().getScheduler()
                            .runTaskLater(plugin, wrapped, delay);
                } else {
                    task = plugin.getServer().getScheduler()
                            .runTask(plugin, wrapped);
                }
            }
            trackTask(task);
            return wrapTrackedTask(task);
        }

        /**
         * Run with access to the BukkitTask (for self-cancellation).
         * Usage: Tasks.timer(20).run(task -> { if (done) task.cancel(); });
         *
         * Note: Bukkit's Consumer<BukkitTask> overloads return void,
         * so this method does too.
         */
        public void run(Consumer<BukkitTask> consumer) {
            ConsumerTaskRunner runner = new ConsumerTaskRunner(consumer, period > 0);
            BukkitTask scheduled;
            if (async) {
                if (period > 0) {
                    scheduled = runner.runTaskTimerAsynchronously(plugin, delay, period);
                } else if (delay > 0) {
                    scheduled = runner.runTaskLaterAsynchronously(plugin, delay);
                } else {
                    scheduled = runner.runTaskAsynchronously(plugin);
                }
            } else {
                if (period > 0) {
                    scheduled = runner.runTaskTimer(plugin, delay, period);
                } else if (delay > 0) {
                    scheduled = runner.runTaskLater(plugin, delay);
                } else {
                    scheduled = runner.runTask(plugin);
                }
            }
            runner.bind(scheduled);
            trackTask(scheduled);
        }
    }

    private static final class ConsumerTaskRunner extends BukkitRunnable {
        private final Consumer<BukkitTask> consumer;
        private final boolean repeating;
        private volatile BukkitTask delegate;

        private ConsumerTaskRunner(Consumer<BukkitTask> consumer, boolean repeating) {
            this.consumer = Objects.requireNonNull(consumer, "consumer");
            this.repeating = repeating;
        }

        private void bind(BukkitTask task) {
            this.delegate = task;
        }

        @Override
        public void run() {
            BukkitTask current = delegate;
            if (current == null) {
                current = plugin.getServer().getScheduler().getCurrentTask();
            }
            BukkitTask tracked = wrapTrackedTask(current);
            try {
                consumer.accept(tracked);
            } finally {
                if (!repeating && current != null) {
                    untrackTask(current);
                }
            }
        }

        @Override
        public synchronized void cancel() throws IllegalStateException {
            try {
                super.cancel();
            } finally {
                BukkitTask current = delegate;
                if (current != null) {
                    untrackTask(current);
                }
            }
        }
    }
}
