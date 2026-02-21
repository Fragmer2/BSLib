package io.github.fragmer2.bslib.api.thread;

import io.github.fragmer2.bslib.api.task.Tasks;
import org.bukkit.Bukkit;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Thread safety utilities for Paper.
 *
 * Check thread:
 *   Async.ensureSync();  // throws if not on main thread
 *   Async.ensureAsync(); // throws if on main thread
 *
 * Safe execution:
 *   Async.sync(() -> player.teleport(loc));   // always runs on main thread
 *   Async.async(() -> loadFromDatabase());     // always runs async
 *
 * Guard (log + auto-fix):
 *   Async.guard(); // logs warning if called from async, auto-fixes by scheduling sync
 *
 * Wrap unsafe call:
 *   Async.syncResult(() -> player.getInventory().getContents())
 *        .thenAccept(items -> processAsync(items));
 */
public final class Async {
    private static Logger logger;
    private static boolean debugMode = false;

    private Async() {}

    public static void init(Logger log) {
        logger = log;
    }

    public static void setDebug(boolean debug) {
        debugMode = debug;
    }

    // ========== Thread checks ==========

    /** True if currently on the main server thread. */
    public static boolean isMainThread() {
        return Bukkit.isPrimaryThread();
    }

    /**
     * Throws if not on main thread. Use before Bukkit API calls.
     */
    public static void ensureSync() {
        if (!isMainThread()) {
            String msg = "Illegal async access to Bukkit API!";
            if (debugMode && logger != null) {
                logger.warning(msg + " at:\n" + getCallerTrace());
            }
            throw new IllegalStateException(msg);
        }
    }

    /**
     * Throws if on main thread. Use before blocking I/O.
     */
    public static void ensureAsync() {
        if (isMainThread()) {
            throw new IllegalStateException("Blocking operation on main thread!");
        }
    }

    // ========== Safe execution ==========

    /**
     * Run on main thread (no-op if already on main).
     */
    public static void sync(Runnable task) {
        if (isMainThread()) {
            task.run();
        } else {
            Tasks.sync().run(task);
        }
    }

    /**
     * Run async (no-op if already async).
     */
    public static void async(Runnable task) {
        if (!isMainThread()) {
            task.run();
        } else {
            Tasks.async().run(task);
        }
    }

    /**
     * Get a value from main thread, usable from async context.
     * Blocks until result is available.
     */
    public static <T> T syncGet(Supplier<T> supplier) {
        if (isMainThread()) {
            return supplier.get();
        }
        java.util.concurrent.CompletableFuture<T> future = new java.util.concurrent.CompletableFuture<>();
        Tasks.sync().run(() -> {
            try {
                future.complete(supplier.get());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get sync result", e);
        }
    }

    /**
     * Async operation → sync callback pattern.
     *   Async.run(
     *       () -> loadFromDB(uuid),          // runs async
     *       data -> applyToPlayer(player, data) // runs sync
     *   );
     */
    public static <T> void run(Supplier<T> asyncWork, Consumer<T> syncCallback) {
        Tasks.async().run(() -> {
            T result = asyncWork.get();
            Tasks.sync().run(() -> syncCallback.accept(result));
        });
    }

    /**
     * Guard — logs warning + stack trace if called from wrong thread.
     * Does NOT throw, just warns. Useful for debugging.
     */
    public static void guard() {
        if (!isMainThread() && logger != null) {
            logger.warning("⚠ Async access detected! This may cause issues.");
            logger.warning("  at: " + getCallerTrace());
        }
    }

    private static String getCallerTrace() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        // Skip first 3 (getStackTrace, getCallerTrace, guard/ensureSync)
        int start = Math.min(3, stack.length);
        int end = Math.min(start + 5, stack.length);
        for (int i = start; i < end; i++) {
            sb.append("\n    ").append(stack[i]);
        }
        return sb.toString();
    }
}
