package io.github.fragmer2.bslib.api.task;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TasksTest {

    private TestRuntime runtime;

    @BeforeEach
    void setUp() {
        runtime = new TestRuntime();
        Tasks.init(runtime.plugin());
    }

    @Test
    void createsTrackedTaskAndAllowsCancellation() {
        FrameworkTask task = Tasks.sync().repeat(20).runTracked(() -> {});

        assertEquals(1, Tasks.trackedTaskCount());
        assertFalse(task.isCancelled());

        task.cancel();

        assertTrue(task.isCancelled());
        assertEquals(0, Tasks.trackedTaskCount());
    }

    @Test
    void delayDoesNotExecuteImmediately() {
        AtomicInteger runs = new AtomicInteger();

        FrameworkTask delayed = Tasks.sync().delay(40).runTracked(runs::incrementAndGet);

        assertEquals(0, runs.get());
        assertEquals(1, Tasks.trackedTaskCount());

        delayed.cancel();
        assertEquals(0, Tasks.trackedTaskCount());
    }

    @Test
    void oneShotTaskExecutesAndIsNotLeaked() {
        AtomicInteger runs = new AtomicInteger();

        FrameworkTask oneShot = Tasks.sync().runTracked(runs::incrementAndGet);

        assertEquals(1, runs.get());
        assertEquals(0, Tasks.trackedTaskCount());
        assertFalse(oneShot.isCancelled());
    }

    @Test
    void separatesAsyncAndSyncSchedulingRoutes() {
        Tasks.sync().repeat(20).runTracked(() -> {});
        Tasks.async().repeat(20).runTracked(() -> {});

        assertTrue(runtime.invoked("runTaskTimer"));
        assertTrue(runtime.invoked("runTaskTimerAsynchronously"));

        assertEquals(2, Tasks.trackedTaskCount());
    }

    @Test
    void consumerTaskCanSelfCancelAndUntrack() {
        FrameworkTask tracked = Tasks.sync().repeat(20).runTracked(task -> task.cancel());

        assertTrue(tracked.isCancelled());
        assertEquals(0, Tasks.trackedTaskCount());
    }

    private static final class TestRuntime {
        private final AtomicInteger ids = new AtomicInteger(1);
        private final Map<Integer, SimpleTask> tasks = new HashMap<>();
        private final Map<String, AtomicInteger> invocations = new HashMap<>();

        private final BukkitScheduler fakeScheduler = (BukkitScheduler) Proxy.newProxyInstance(
                BukkitScheduler.class.getClassLoader(),
                new Class[]{BukkitScheduler.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    invocations.computeIfAbsent(name, k -> new AtomicInteger()).incrementAndGet();

                    return switch (name) {
                        case "runTask", "runTaskAsynchronously" -> schedule((Runnable) args[1], false, true, false);
                        case "runTaskLater", "runTaskLaterAsynchronously" -> schedule((Runnable) args[1], false, false, false);
                        case "runTaskTimer", "runTaskTimerAsynchronously" -> schedule((Runnable) args[1], true, false, false);
                        case "isQueued" -> {
                            Integer id = (Integer) args[0];
                            SimpleTask task = tasks.get(id);
                            yield task != null && task.queued.get();
                        }
                        case "isCurrentlyRunning" -> false;
                        default -> throw new UnsupportedOperationException("Unsupported scheduler method in test runtime: " + name);
                    };
                }
        );

        private final Server fakeServer = (Server) Proxy.newProxyInstance(
                Server.class.getClassLoader(),
                new Class[]{Server.class},
                (proxy, method, args) -> {
                    if ("getScheduler".equals(method.getName())) {
                        return fakeScheduler;
                    }
                    throw new UnsupportedOperationException("Unsupported server method in test runtime: " + method.getName());
                }
        );

        private final Plugin fakePlugin = (Plugin) Proxy.newProxyInstance(
                Plugin.class.getClassLoader(),
                new Class[]{Plugin.class},
                (proxy, method, args) -> {
                    return switch (method.getName()) {
                        case "getServer" -> fakeServer;
                        case "isEnabled" -> true;
                        case "getName" -> "tasks-test-plugin";
                        default -> null;
                    };
                }
        );

        Plugin plugin() {
            return fakePlugin;
        }

        boolean invoked(String name) {
            return invocations.getOrDefault(name, new AtomicInteger()).get() > 0;
        }

        private BukkitTask schedule(Runnable runnable, boolean repeating, boolean executeNow, boolean sync) {
            int id = ids.getAndIncrement();
            SimpleTask task = new SimpleTask(id, fakePlugin, sync, repeating);
            tasks.put(id, task);

            if (executeNow) {
                runnable.run();
                if (!repeating && !task.cancelled.get()) {
                    task.queued.set(false);
                }
            }

            return task;
        }
    }

    private static final class SimpleTask implements BukkitTask {
        private final int id;
        private final Plugin owner;
        private final boolean sync;
        private final boolean repeating;
        private final AtomicBoolean queued = new AtomicBoolean(true);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);

        private SimpleTask(int id, Plugin owner, boolean sync, boolean repeating) {
            this.id = id;
            this.owner = owner;
            this.sync = sync;
            this.repeating = repeating;
        }

        @Override
        public int getTaskId() {
            return id;
        }

        @Override
        public Plugin getOwner() {
            return owner;
        }

        @Override
        public boolean isSync() {
            return sync;
        }

        @Override
        public void cancel() {
            cancelled.set(true);
            queued.set(false);
        }

        @Override
        public boolean isCancelled() {
            return cancelled.get();
        }

        @Override
        public String toString() {
            return "SimpleTask{" +
                    "id=" + id +
                    ", repeating=" + repeating +
                    '}';
        }
    }
}
