package io.github.fragmer2.bslib.api.thread;

import io.github.fragmer2.bslib.api.task.Tasks;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Tick-safe async computation chain.
 * Dev never thinks about threads — the framework handles everything.
 *
 * ===== Basic: compute async → use result sync =====
 *   Tasks.compute(() -> heavyDatabaseQuery())
 *        .thenSync(result -> player.sendMessage("Result: " + result));
 *
 * ===== Chain: async → transform async → use sync =====
 *   Tasks.compute(() -> loadFromDB(uuid))
 *        .thenAsync(data -> enrichWithAPI(data))
 *        .thenSync(enriched -> applyToPlayer(player, enriched));
 *
 * ===== Error handling =====
 *   Tasks.compute(() -> riskyOperation())
 *        .thenSync(result -> useResult(result))
 *        .onError(error -> player.sendMessage("Failed: " + error.getMessage()));
 *
 * ===== Timeout =====
 *   Tasks.compute(() -> slowOperation())
 *        .timeout(5, TimeUnit.SECONDS)
 *        .thenSync(result -> use(result))
 *        .onTimeout(() -> player.sendMessage("Timed out!"));
 *
 * ===== With Reactive =====
 *   Tasks.compute(() -> fetchBalance(uuid))
 *        .thenSync(balance::set);  // sets Reactive → triggers all bindings
 *
 * ===== Cancel =====
 *   AsyncChain<?> chain = Tasks.compute(() -> longTask());
 *   chain.cancel();
 */
public class AsyncChain<T> {
    private final Supplier<T> computation;
    private Consumer<T> syncCallback;
    private Consumer<Throwable> errorHandler;
    private Runnable timeoutHandler;
    private long timeoutMs = 0;
    private Function<T, ?> asyncTransform;
    private AsyncChain<?> next; // linked chain
    private volatile boolean cancelled = false;
    private volatile Future<?> future;

    AsyncChain(Supplier<T> computation) {
        this.computation = computation;
    }

    // ========== Chain: async → sync ==========

    /**
     * Process the result on the main thread (sync).
     * This is the final step — executes the chain.
     */
    public AsyncChain<T> thenSync(Consumer<T> callback) {
        this.syncCallback = callback;
        execute();
        return this;
    }

    /**
     * Transform the result on another async thread, returning a new chain.
     */
    @SuppressWarnings("unchecked")
    public <R> AsyncChain<R> thenAsync(Function<T, R> transform) {
        AsyncChain<R> nextChain = new AsyncChain<>(() -> {
            // This supplier will be replaced during execution
            throw new IllegalStateException("Should not be called directly");
        });
        this.asyncTransform = (Function<T, ?>) transform;
        this.next = nextChain;
        return nextChain;
    }

    // ========== Error / Timeout ==========

    /**
     * Handle errors that occur during computation.
     * Runs on main thread.
     */
    public AsyncChain<T> onError(Consumer<Throwable> handler) {
        this.errorHandler = handler;
        return this;
    }

    /**
     * Set a timeout for the computation.
     */
    public AsyncChain<T> timeout(long amount, TimeUnit unit) {
        this.timeoutMs = unit.toMillis(amount);
        return this;
    }

    /**
     * Handle timeout. Runs on main thread.
     */
    public AsyncChain<T> onTimeout(Runnable handler) {
        this.timeoutHandler = handler;
        return this;
    }

    // ========== Cancel ==========

    public void cancel() {
        cancelled = true;
        if (future != null) {
            future.cancel(true);
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    // ========== Execution ==========

    @SuppressWarnings("unchecked")
    private void execute() {
        Tasks.async().run(() -> {
            if (cancelled) return;

            try {
                T result;

                if (timeoutMs > 0) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<T> f = executor.submit(computation::get);
                    this.future = f;
                    try {
                        result = f.get(timeoutMs, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        f.cancel(true);
                        executor.shutdownNow();
                        if (!cancelled && timeoutHandler != null) {
                            Tasks.sync().run(timeoutHandler);
                        }
                        return;
                    } finally {
                        executor.shutdownNow();
                    }
                } else {
                    result = computation.get();
                }

                if (cancelled) return;

                // If there's an async transform chain
                if (asyncTransform != null && next != null) {
                    Object transformed = ((Function<T, Object>) asyncTransform).apply(result);
                    if (cancelled) return;

                    // Continue on the next chain
                    AsyncChain<Object> nextTyped = (AsyncChain<Object>) next;
                    if (nextTyped.syncCallback != null) {
                        Tasks.sync().run(() -> {
                            if (!cancelled) {
                                try {
                                    nextTyped.syncCallback.accept(transformed);
                                } catch (Exception e) {
                                    if (nextTyped.errorHandler != null) {
                                        nextTyped.errorHandler.accept(e);
                                    } else if (errorHandler != null) {
                                        errorHandler.accept(e);
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    return;
                }

                // Direct sync callback
                if (syncCallback != null) {
                    Tasks.sync().run(() -> {
                        if (!cancelled) {
                            try {
                                syncCallback.accept(result);
                            } catch (Exception e) {
                                if (errorHandler != null) {
                                    errorHandler.accept(e);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

            } catch (Exception e) {
                if (!cancelled) {
                    if (errorHandler != null) {
                        Tasks.sync().run(() -> errorHandler.accept(e));
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
