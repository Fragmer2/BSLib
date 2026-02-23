package io.github.fragmer2.bslib.api.task;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TasksTest {

    @BeforeEach
    void setUp() {
        FakeRuntime runtime = new FakeRuntime();
        Tasks.init(runtime.plugin());
        Tasks.trackedTaskCount();
    }

    @Test
    void tracksAndRemovesTasksAcrossAllSchedulingModes() {
        FakeRuntime runtime = new FakeRuntime();
        Tasks.init(runtime.plugin());

        List<BukkitTask> cancellable = new ArrayList<>();
        cancellable.add(Tasks.sync().repeat(20).run(() -> {}));
        cancellable.add(Tasks.sync().delay(20).repeat(20).run(() -> {}));
        cancellable.add(Tasks.async().repeat(20).run(() -> {}));
        cancellable.add(Tasks.async().delay(20).repeat(20).run(() -> {}));

        AtomicInteger consumerRuns = new AtomicInteger();
        Tasks.sync().run(task -> consumerRuns.incrementAndGet());
        Tasks.async().run(task -> consumerRuns.incrementAndGet());
        Tasks.sync().delay(10).run(task -> consumerRuns.incrementAndGet());
        Tasks.async().delay(10).run(task -> consumerRuns.incrementAndGet());

        assertEquals(4, Tasks.trackedTaskCount());
        assertEquals(4, consumerRuns.get());

        cancellable.forEach(BukkitTask::cancel);
        assertEquals(0, Tasks.trackedTaskCount());
    }

    @Test
    void stressTenThousandTasksCancelWithoutLeaks() {
        FakeRuntime runtime = new FakeRuntime();
        Tasks.init(runtime.plugin());

        List<BukkitTask> tasks = new ArrayList<>();
        for (int i = 0; i < 2500; i++) {
            tasks.add(Tasks.sync().repeat(20).run(() -> {}));
            tasks.add(Tasks.async().repeat(20).run(() -> {}));
            Tasks.sync().run((Consumer<BukkitTask>) BukkitTask::cancel);
            Tasks.async().run((Consumer<BukkitTask>) BukkitTask::cancel);
        }

        assertEquals(5000, Tasks.trackedTaskCount());

        tasks.forEach(BukkitTask::cancel);
        assertEquals(0, Tasks.trackedTaskCount());
    }

    private static final class FakeRuntime {
        private final AtomicInteger nextId = new AtomicInteger(1);
        private final Map<Integer, TaskState> states = new ConcurrentHashMap<>();
        private final ThreadLocal<Integer> currentTaskId = new ThreadLocal<>();

        private final BukkitScheduler scheduler = (BukkitScheduler) Proxy.newProxyInstance(
                BukkitScheduler.class.getClassLoader(),
                new Class[]{BukkitScheduler.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    return switch (name) {
                        case "runTask", "runTaskAsynchronously", "runTaskLater", "runTaskLaterAsynchronously" -> {
                            Object action = args[1];
                            if (action instanceof Runnable runnable) {
                                yield scheduleRunnable(runnable, false);
                            }
                            if (action instanceof Consumer<?> consumer) {
                                @SuppressWarnings("unchecked")
                                Consumer<BukkitTask> typed = (Consumer<BukkitTask>) consumer;
                                yield scheduleConsumer(typed, false);
                            }
                            throw new IllegalArgumentException("Unsupported action type: " + action);
                        }
                        case "runTaskTimer", "runTaskTimerAsynchronously" -> {
                            Object action = args[1];
                            if (action instanceof Runnable runnable) {
                                yield scheduleRunnable(runnable, true);
                            }
                            if (action instanceof Consumer<?> consumer) {
                                @SuppressWarnings("unchecked")
                                Consumer<BukkitTask> typed = (Consumer<BukkitTask>) consumer;
                                yield scheduleConsumer(typed, true);
                            }
                            throw new IllegalArgumentException("Unsupported action type: " + action);
                        }
                        case "isQueued" -> {
                            Integer id = (Integer) args[0];
                            TaskState state = states.get(id);
                            yield state != null && state.queued;
                        }
                        case "isCurrentlyRunning" -> {
                            Integer id = (Integer) args[0];
                            yield id.equals(currentTaskId.get());
                        }
                        case "getCurrentTask" -> {
                            Integer id = currentTaskId.get();
                            yield id == null ? null : states.get(id).task;
                        }
                        default -> throw new UnsupportedOperationException("Unsupported scheduler method: " + name);
                    };
                }
        );

        private final Server server = (Server) Proxy.newProxyInstance(
                Server.class.getClassLoader(),
                new Class[]{Server.class},
                (proxy, method, args) -> {
                    if ("getScheduler".equals(method.getName())) {
                        return scheduler;
                    }
                    throw new UnsupportedOperationException("Unsupported server method: " + method.getName());
                }
        );

        private final Plugin plugin = (Plugin) Proxy.newProxyInstance(
                Plugin.class.getClassLoader(),
                new Class[]{Plugin.class},
                (proxy, method, args) -> {
                    if ("getServer".equals(method.getName())) {
                        return server;
                    }
                    if ("isEnabled".equals(method.getName())) {
                        return true;
                    }
                    if ("getName".equals(method.getName())) {
                        return "test-plugin";
                    }
                    throw new UnsupportedOperationException("Unsupported plugin method: " + method.getName());
                }
        );

        private BukkitTask scheduleRunnable(Runnable runnable, boolean repeating) {
            int id = nextId.getAndIncrement();
            TaskState state = new TaskState(id, repeating);
            states.put(id, state);
            if (!repeating) {
                runTaskNow(state, runnable);
            }
            return state.task;
        }

        private BukkitTask scheduleConsumer(Consumer<BukkitTask> consumer, boolean repeating) {
            int id = nextId.getAndIncrement();
            TaskState state = new TaskState(id, repeating);
            states.put(id, state);
            runTaskNow(state, () -> consumer.accept(state.task));
            return state.task;
        }

        private void runTaskNow(TaskState state, Runnable action) {
            currentTaskId.set(state.id);
            try {
                action.run();
            } finally {
                currentTaskId.remove();
                if (!state.repeating || state.cancelled) {
                    state.queued = false;
                }
            }
        }

        private Plugin plugin() {
            return plugin;
        }

        private final class TaskState {
            private final int id;
            private final boolean repeating;
            private volatile boolean queued = true;
            private volatile boolean cancelled = false;
            private final BukkitTask task;

            private TaskState(int id, boolean repeating) {
                this.id = id;
                this.repeating = repeating;
                this.task = (BukkitTask) Proxy.newProxyInstance(
                        BukkitTask.class.getClassLoader(),
                        new Class[]{BukkitTask.class},
                        (proxy, method, args) -> {
                            String name = method.getName();
                            return switch (name) {
                                case "getTaskId" -> id;
                                case "cancel" -> {
                                    cancelled = true;
                                    queued = false;
                                    yield null;
                                }
                                case "isCancelled" -> cancelled;
                                case "getOwner" -> plugin;
                                case "isSync" -> true;
                                case "equals" -> proxy == args[0];
                                case "hashCode" -> System.identityHashCode(proxy);
                                case "toString" -> "FakeTask(" + id + ")";
                                default -> throw new UnsupportedOperationException("Unsupported task method: " + name);
                            };
                        }
                );
            }
        }
    }
}
