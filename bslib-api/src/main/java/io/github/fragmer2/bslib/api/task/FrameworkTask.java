package io.github.fragmer2.bslib.api.task;

/**
 * Framework-owned task handle used by BSLib task APIs.
 *
 * <p>This wrapper is stable for diagnostics and lifecycle management and does not rely on
 * scheduler-specific runtime lookup methods.</p>
 */
public interface FrameworkTask {
    int getTaskId();
    void cancel();
    boolean isCancelled();
}
