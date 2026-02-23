package io.github.fragmer2.bslib.api.task;

public interface SchedulerAdapter {
    FrameworkTask runSync(Runnable task);
    FrameworkTask runLater(Runnable task, long delay);
    FrameworkTask runTimer(Runnable task, long delay, long period);

    default FrameworkTask runAsync(Runnable task) {
        return runSync(task);
    }

    default FrameworkTask runLaterAsync(Runnable task, long delay) {
        return runLater(task, delay);
    }

    default FrameworkTask runTimerAsync(Runnable task, long delay, long period) {
        return runTimer(task, delay, period);
    }

    default boolean isTaskActive(int taskId) {
        return false;
    }
}
