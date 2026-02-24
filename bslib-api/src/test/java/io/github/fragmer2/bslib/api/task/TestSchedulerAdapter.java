package io.github.fragmer2.bslib.api.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class TestSchedulerAdapter implements SchedulerAdapter {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final ConcurrentMap<Integer, MemoryTask> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger syncRuns = new AtomicInteger();
    private final AtomicInteger asyncRuns = new AtomicInteger();
    private long tick;

    @Override
    public FrameworkTask runSync(Runnable task) {
        return schedule(task, false, 0L, -1L, true);
    }

    @Override
    public FrameworkTask runLater(Runnable task, long delay) {
        return schedule(task, false, delay, -1L, true);
    }

    @Override
    public FrameworkTask runTimer(Runnable task, long delay, long period) {
        return schedule(task, false, delay, period, true);
    }

    @Override
    public FrameworkTask runAsync(Runnable task) {
        return schedule(task, true, 0L, -1L, true);
    }

    @Override
    public FrameworkTask runLaterAsync(Runnable task, long delay) {
        return schedule(task, true, delay, -1L, true);
    }

    @Override
    public FrameworkTask runTimerAsync(Runnable task, long delay, long period) {
        return schedule(task, true, delay, period, true);
    }

    @Override
    public boolean isTaskActive(int taskId) {
        MemoryTask task = tasks.get(taskId);
        return task != null && task.active.get() && !task.cancelled.get();
    }

    int syncRunCount() {
        return syncRuns.get();
    }

    int asyncRunCount() {
        return asyncRuns.get();
    }

    void tick(long ticks) {
        for (int i = 0; i < ticks; i++) {
            tick++;
            List<MemoryTask> due = new ArrayList<>();
            for (MemoryTask task : tasks.values()) {
                if (task.active.get() && !task.cancelled.get() && task.nextRunTick <= tick) {
                    due.add(task);
                }
            }
            for (MemoryTask task : due) {
                task.execute();
            }
            cleanupInactive();
        }
    }

    private FrameworkTask schedule(Runnable runnable, boolean async, long delay, long period, boolean active) {
        int id = nextId.getAndIncrement();
        MemoryTask task = new MemoryTask(id, runnable, async, period, tick + Math.max(0, delay), active);
        tasks.put(id, task);
        if (delay <= 0 && period < 0) {
            task.execute();
            cleanupInactive();
        }
        return task;
    }

    private void cleanupInactive() {
        Iterator<MemoryTask> it = tasks.values().iterator();
        while (it.hasNext()) {
            MemoryTask task = it.next();
            if (!task.active.get() || task.cancelled.get()) {
                it.remove();
            }
        }
    }

    private final class MemoryTask implements FrameworkTask {
        private final int id;
        private final Runnable runnable;
        private final boolean async;
        private final long period;
        private volatile long nextRunTick;
        private final AtomicBoolean active;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);

        private MemoryTask(int id, Runnable runnable, boolean async, long period, long nextRunTick, boolean active) {
            this.id = id;
            this.runnable = runnable;
            this.async = async;
            this.period = period;
            this.nextRunTick = nextRunTick;
            this.active = new AtomicBoolean(active);
        }

        private void execute() {
            if (cancelled.get() || !active.get()) {
                return;
            }
            if (async) {
                asyncRuns.incrementAndGet();
            } else {
                syncRuns.incrementAndGet();
            }

            runnable.run();

            if (period > 0 && !cancelled.get()) {
                nextRunTick = tick + period;
            } else {
                active.set(false);
            }
        }

        @Override
        public int getTaskId() {
            return id;
        }

        @Override
        public void cancel() {
            cancelled.set(true);
            active.set(false);
        }

        @Override
        public boolean isCancelled() {
            return cancelled.get();
        }
    }
}
