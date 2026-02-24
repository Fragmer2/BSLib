package io.github.fragmer2.bslib.api.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

final class BukkitSchedulerAdapter implements SchedulerAdapter {
    private final Plugin plugin;

    BukkitSchedulerAdapter(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    @Override
    public FrameworkTask runSync(Runnable task) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTask(plugin, task);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public FrameworkTask runLater(Runnable task, long delay) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskLater(plugin, task, delay);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public FrameworkTask runTimer(Runnable task, long delay, long period) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, task, delay, period);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public FrameworkTask runAsync(Runnable task) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public FrameworkTask runLaterAsync(Runnable task, long delay) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public FrameworkTask runTimerAsync(Runnable task, long delay, long period) {
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
        return new BukkitFrameworkTask(bukkitTask);
    }

    @Override
    public boolean isTaskActive(int taskId) {
        return plugin.getServer().getScheduler().isQueued(taskId)
                || plugin.getServer().getScheduler().isCurrentlyRunning(taskId);
    }

    private static final class BukkitFrameworkTask implements FrameworkTask {
        private final BukkitTask task;

        private BukkitFrameworkTask(BukkitTask task) {
            this.task = task;
        }

        @Override
        public int getTaskId() {
            return task.getTaskId();
        }

        @Override
        public void cancel() {
            task.cancel();
        }

        @Override
        public boolean isCancelled() {
            return task.isCancelled();
        }

        private BukkitTask raw() {
            return task;
        }
    }

    static BukkitTask unwrap(FrameworkTask task) {
        if (task instanceof BukkitFrameworkTask wrapped) {
            return wrapped.raw();
        }
        throw new IllegalStateException("Cannot unwrap non-Bukkit FrameworkTask: " + task.getClass().getName());
    }
}
