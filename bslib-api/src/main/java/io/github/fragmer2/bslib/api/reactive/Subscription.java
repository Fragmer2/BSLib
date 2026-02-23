package io.github.fragmer2.bslib.api.reactive;

/**
 * Disposable subscription handle.
 */
@FunctionalInterface
public interface Subscription extends AutoCloseable {
    void unsubscribe();

    @Override
    default void close() {
        unsubscribe();
    }
}
