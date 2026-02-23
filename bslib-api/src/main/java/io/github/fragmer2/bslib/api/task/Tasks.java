package io.github.fragmer2.bslib.api.task;

import io.github.fragmer2.bslib.internal.error.FrameworkExceptionHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Clean scheduler API.
 */
public final class Tasks {
    private static Plugin plugin;
    private static SchedulerAdapter scheduler;
    private static final ConcurrentMap<Integer, FrameworkTask> trackedTasks = new ConcurrentHashMap<>();

    private Tasks() {}

    public static void init(Plugin p) {
        plugin = p;
        if (scheduler == null && p != null) {
            scheduler = new BukkitSchedulerAdapter(p);
        }
    }

    public static void setScheduler(SchedulerAdapter adapter) {
        scheduler = Objects.requireNonNull(adapter, "adapter");
    }

    private static SchedulerAdapter scheduler() {
        if (scheduler == null) {
            if (plugin == null) {
                throw new IllegalStateException("Tasks scheduler is not initialized. Call Tasks.init(plugin) or Tasks.setScheduler(adapter)");
            }
            scheduler = new BukkitSchedulerAdapter(plugin);
        }
        return scheduler;
    }

    public static int trackedTaskCount() {
        cleanupTrackedTasks();
        return trackedTasks.size();
    }

    private static FrameworkTask register(FrameworkTask task) {
        trackedTasks.put(task.getTaskId(), task);
        return task;
    }

    private static void unregister(int taskId) {
        trackedTasks.remove(taskId);
    }

    private static void cleanupTrackedTasks() {
        SchedulerAdapter adapter = scheduler;
        if (adapter == null) {
            return;
        }
        trackedTasks.entrySet().removeIf(entry -> !adapter.isTaskActive(entry.getKey()));
    }

    public static TaskBuilder sync() {
        return new TaskBuilder(false);
    }

    public static TaskBuilder async() {
        return new TaskBuilder(true);
    }

    public static TaskBuilder timer(long intervalTicks) {
        return new TaskBuilder(false).repeat(intervalTicks);
    }

    public static <T> io.github.fragmer2.bslib.api.thread.AsyncChain<T> compute(java.util.function.Supplier<T> supplier) {
        return new io.github.fragmer2.bslib.api.thread.AsyncChain<>(supplier);
    }

    public static final class TaskBuilder {
        private final boolean async;
        private long delay = 0;
        private long period = -1;

        TaskBuilder(boolean async) {
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

        public FrameworkTask runTracked(Runnable runnable) {
            Objects.requireNonNull(runnable, "runnable");
            boolean repeating = period > 0;
            FrameworkTask scheduled = scheduleRunnable(wrapRunnable(runnable, repeating));
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

        public FrameworkTask runTracked(Consumer<BukkitTask> consumer) {
            Objects.requireNonNull(consumer, "consumer");
            boolean repeating = period > 0;

            AtomicReference<FrameworkTask> ref = new AtomicReference<>();
            MutableBukkitTaskView view = new MutableBukkitTaskView(ref);
            Runnable callback = wrapRunnable(() -> consumer.accept(view), repeating);

            FrameworkTask scheduled = scheduleRunnable(callback);
            ref.set(scheduled);
            FrameworkTask tracked = register(scheduled);
            if (!repeating) {
                cleanupTrackedTasks();
            }
            return tracked;
        }

        private FrameworkTask scheduleRunnable(Runnable runnable) {
            SchedulerAdapter adapter = scheduler();
            if (async) {
                if (period > 0) {
                    return adapter.runTimerAsync(runnable, delay, period);
                }
                if (delay > 0) {
                    return adapter.runLaterAsync(runnable, delay);
                }
                return adapter.runAsync(runnable);
            }

            if (period > 0) {
                return adapter.runTimer(runnable, delay, period);
            }
            if (delay > 0) {
                return adapter.runLater(runnable, delay);
            }
            return adapter.runSync(runnable);
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
        if (frameworkTask == null) {
            return null;
        }
        if (frameworkTask.getClass().getName().contains("BukkitFrameworkTask")) {
            return BukkitSchedulerAdapter.unwrap(frameworkTask);
        }
        return new MutableBukkitTaskView(new AtomicReference<>(frameworkTask));
    }

    private static final class MutableBukkitTaskView implements BukkitTask {
        private final AtomicReference<FrameworkTask> delegate;

        private MutableBukkitTaskView(AtomicReference<FrameworkTask> delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getTaskId() {
            FrameworkTask task = delegate.get();
            return task != null ? task.getTaskId() : -1;
        }

        @Override
        public Plugin getOwner() {
            return plugin;
        }

        @Override
        public boolean isSync() {
            return true;
        }

        @Override
        public void cancel() {
            FrameworkTask task = delegate.get();
            if (task != null) {
                task.cancel();
                unregister(task.getTaskId());
            }
        }

        @Override
        public boolean isCancelled() {
            FrameworkTask task = delegate.get();
            return task != null && task.isCancelled();
        }
    }
}
