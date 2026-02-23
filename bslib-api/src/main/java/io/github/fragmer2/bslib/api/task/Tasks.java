package io.github.fragmer2.bslib.api.task;

import io.github.fragmer2.bslib.internal.error.FrameworkExceptionHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * Clean scheduler API.
 */
public final class Tasks {
    private static Plugin plugin;
    private static final ConcurrentMap<Integer, FrameworkTaskImpl> trackedTasks = new ConcurrentHashMap<>();

    private Tasks() {}

    public static void init(Plugin p) {
        plugin = p;
    }

    public static int trackedTaskCount() {
        cleanupTrackedTasks();
        return trackedTasks.size();
    }

    private static FrameworkTask register(BukkitTask task) {
        FrameworkTaskImpl wrapped = new FrameworkTaskImpl(task);
        trackedTasks.put(task.getTaskId(), wrapped);
        return wrapped;
    }

    private static void unregister(int taskId) {
        trackedTasks.remove(taskId);
    }

    private static void cleanupTrackedTasks() {
        if (plugin == null) {
            return;
        }
        trackedTasks.entrySet().removeIf(entry -> {
            int id = entry.getKey();
            return !plugin.getServer().getScheduler().isQueued(id)
                    && !plugin.getServer().getScheduler().isCurrentlyRunning(id);
        });
    }

    public static TaskBuilder sync() {
        return new TaskBuilder(plugin, false);
    }

    public static TaskBuilder async() {
        return new TaskBuilder(plugin, true);
    }

    public static TaskBuilder timer(long intervalTicks) {
        return new TaskBuilder(plugin, false).repeat(intervalTicks);
    }

    public static <T> io.github.fragmer2.bslib.api.thread.AsyncChain<T> compute(java.util.function.Supplier<T> supplier) {
        return new io.github.fragmer2.bslib.api.thread.AsyncChain<>(supplier);
    }

    public static final class TaskBuilder {
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
         * Legacy API for Bukkit compatibility.
         * Prefer {@link #runTracked(Runnable)} in new code.
         */
        public BukkitTask run(Runnable runnable) {
            return asBukkitTask(runTracked(runnable));
        }

        /**
         * Schedule a task and return framework-owned handle for lifecycle-safe diagnostics.
         */
        public FrameworkTask runTracked(Runnable runnable) {
            Objects.requireNonNull(runnable, "runnable");
            boolean repeating = period > 0;
            Runnable guarded = wrapRunnable(runnable, repeating);
            BukkitTask scheduled = schedule(guarded);
            FrameworkTask tracked = register(scheduled);
            if (!repeating) {
                cleanupTrackedTasks();
            }
            return tracked;
        }

        /**
         * Legacy API for Bukkit compatibility.
         * Prefer {@link #runTracked(Consumer)} in new code.
         */
        public void run(Consumer<BukkitTask> consumer) {
            runTracked(consumer);
        }

        /**
         * Schedule task with access to cancellable Bukkit handle.
         */
        public FrameworkTask runTracked(Consumer<BukkitTask> consumer) {
            Objects.requireNonNull(consumer, "consumer");
            boolean repeating = period > 0;
            ConsumerTaskRunner runner = new ConsumerTaskRunner(consumer, repeating);
            BukkitTask scheduled = schedule(runner);
            runner.bind(scheduled);
            FrameworkTask tracked = register(scheduled);
            if (!repeating) {
                cleanupTrackedTasks();
            }
            return tracked;
        }

        private BukkitTask schedule(Runnable runnable) {
            if (async) {
                if (period > 0) {
                    return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
                }
                if (delay > 0) {
                    return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
                }
                return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
            }
            if (period > 0) {
                return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
            }
            if (delay > 0) {
                return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
            }
            return plugin.getServer().getScheduler().runTask(plugin, runnable);
        }

        private BukkitTask schedule(BukkitRunnable runnable) {
            if (async) {
                if (period > 0) {
                    return runnable.runTaskTimerAsynchronously(plugin, delay, period);
                }
                if (delay > 0) {
                    return runnable.runTaskLaterAsynchronously(plugin, delay);
                }
                return runnable.runTaskAsynchronously(plugin);
            }
            if (period > 0) {
                return runnable.runTaskTimer(plugin, delay, period);
            }
            if (delay > 0) {
                return runnable.runTaskLater(plugin, delay);
            }
            return runnable.runTask(plugin);
        }

        private Runnable wrapRunnable(Runnable runnable, boolean repeating) {
            return () -> {
                try {
                    runnable.run();
                } catch (Throwable error) {
                    FrameworkExceptionHandler.handle(plugin, FrameworkExceptionHandler.Source.TASK, error,
                            Map.of("source", "task", "mode", repeating ? "repeating" : "oneshot"));
                } finally {
                    if (!repeating) {
                        cleanupTrackedTasks();
                    }
                }
            };
        }
    }

    private static BukkitTask asBukkitTask(FrameworkTask frameworkTask) {
        FrameworkTaskImpl wrapped = (FrameworkTaskImpl) frameworkTask;
        return new BukkitTask() {
            @Override
            public int getTaskId() {
                return wrapped.getTaskId();
            }

            @Override
            public Plugin getOwner() {
                return wrapped.delegate.getOwner();
            }

            @Override
            public boolean isSync() {
                return wrapped.delegate.isSync();
            }

            @Override
            public void cancel() {
                wrapped.cancel();
            }

            @Override
            public boolean isCancelled() {
                return wrapped.isCancelled();
            }
        };
    }

    private static final class FrameworkTaskImpl implements FrameworkTask {
        private final BukkitTask delegate;

        private FrameworkTaskImpl(BukkitTask delegate) {
            this.delegate = Objects.requireNonNull(delegate, "delegate");
        }

        @Override
        public int getTaskId() {
            return delegate.getTaskId();
        }

        @Override
        public void cancel() {
            try {
                delegate.cancel();
            } finally {
                unregister(delegate.getTaskId());
            }
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }
    }

    private static final class ConsumerTaskRunner extends BukkitRunnable {
        private final Consumer<BukkitTask> consumer;
        private final boolean repeating;
        private volatile BukkitTask delegate;

        private ConsumerTaskRunner(Consumer<BukkitTask> consumer, boolean repeating) {
            this.consumer = consumer;
            this.repeating = repeating;
        }

        private void bind(BukkitTask task) {
            this.delegate = task;
        }

        @Override
        public void run() {
            try {
                consumer.accept(delegate);
            } catch (Throwable error) {
                FrameworkExceptionHandler.handle(plugin, FrameworkExceptionHandler.Source.TASK, error,
                        Map.of("source", "task", "mode", repeating ? "repeating" : "oneshot"));
            } finally {
                if (!repeating) {
                    cleanupTrackedTasks();
                }
            }
        }

        @Override
        public synchronized void cancel() throws IllegalStateException {
            try {
                super.cancel();
            } finally {
                if (delegate != null) {
                    unregister(delegate.getTaskId());
                }
            }
        }
    }
}
