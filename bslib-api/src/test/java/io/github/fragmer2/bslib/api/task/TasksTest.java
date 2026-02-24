package io.github.fragmer2.bslib.api.task;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TasksTest {

    private TestSchedulerAdapter scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new TestSchedulerAdapter();
        Tasks.setScheduler(scheduler);
        Tasks.init(fakePlugin());
    }

    @Test
    void createsTaskAndTracksActiveCount() {
        FrameworkTask task = Tasks.sync().repeat(20).runTracked(() -> {});

        assertEquals(1, Tasks.trackedTaskCount());
        assertFalse(task.isCancelled());

        task.cancel();

        assertTrue(task.isCancelled());
        assertEquals(0, Tasks.trackedTaskCount());
    }

    @Test
    void delayedTaskExecutesOnlyAfterTicks() {
        AtomicInteger runs = new AtomicInteger();

        FrameworkTask task = Tasks.sync().delay(5).runTracked(runs::incrementAndGet);

        assertEquals(0, runs.get());
        assertEquals(1, Tasks.trackedTaskCount());

        scheduler.tick(4);
        assertEquals(0, runs.get());
        assertEquals(1, Tasks.trackedTaskCount());

        scheduler.tick(1);
        assertEquals(1, runs.get());
        assertEquals(0, Tasks.trackedTaskCount());
        assertFalse(task.isCancelled());
    }

    @Test
    void oneShotTaskRunsImmediatelyAndDoesNotLeak() {
        AtomicInteger runs = new AtomicInteger();

        FrameworkTask task = Tasks.sync().runTracked(runs::incrementAndGet);

        assertEquals(1, runs.get());
        assertEquals(0, Tasks.trackedTaskCount());
        assertFalse(task.isCancelled());
    }

    @Test
    void separatesSyncAndAsyncExecutionPaths() {
        AtomicInteger syncRuns = new AtomicInteger();
        AtomicInteger asyncRuns = new AtomicInteger();

        Tasks.sync().repeat(20).runTracked(syncRuns::incrementAndGet);
        Tasks.async().repeat(20).runTracked(asyncRuns::incrementAndGet);

        scheduler.tick(1);

        assertEquals(1, syncRuns.get());
        assertEquals(1, asyncRuns.get());
        assertTrue(scheduler.syncRunCount() >= 1);
        assertTrue(scheduler.asyncRunCount() >= 1);
    }

    @Test
    void consumerCanCancelRepeatingTask() {
        FrameworkTask[] holder = new FrameworkTask[1];
        holder[0] = Tasks.sync().repeat(1).runTracked(task -> task.cancel());

        scheduler.tick(1);

        assertTrue(holder[0].isCancelled());
        assertEquals(0, Tasks.trackedTaskCount());
    }

    private Plugin fakePlugin() {
        return (Plugin) Proxy.newProxyInstance(
                Plugin.class.getClassLoader(),
                new Class[]{Plugin.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getName" -> "tasks-test-plugin";
                    case "isEnabled" -> true;
                    default -> null;
                }
        );
    }
}
