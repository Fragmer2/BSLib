package io.github.fragmer2.bslib.api.reactive;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ReactiveTest {

    @Test
    void concurrentUpdateMaintainsCorrectFinalValue() throws Exception {
        Reactive<Integer> value = Reactive.of(0);

        int threads = 8;
        int perThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                ready.countDown();
                start.await();
                for (int j = 0; j < perThread; j++) {
                    value.update(v -> v + 1);
                }
                return null;
            });
        }

        ready.await(2, TimeUnit.SECONDS);
        start.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(threads * perThread, value.get());
        assertEquals(threads * perThread, value.version());
    }

    @Test
    void unsubscribeStopsListenerRetention() {
        Reactive<Integer> source = Reactive.of(1);
        AtomicInteger seen = new AtomicInteger(0);

        Subscription sub = source.subscribeSet(v -> seen.incrementAndGet());
        sub.unsubscribe();

        source.set(2);
        assertEquals(0, seen.get());
    }

    @Test
    void derivedReactiveDestroyUnsubscribesFromParent() {
        Reactive<Integer> source = Reactive.of(10);
        Reactive<Integer> derived = source.distinctUntilChanged();
        AtomicInteger seen = new AtomicInteger(0);

        derived.subscribeSet(v -> seen.incrementAndGet());
        derived.destroy();

        source.set(11);
        assertEquals(0, seen.get());
        assertFalse(source.isDestroyed());
    }
}
