package io.github.fragmer2.bslib.api.messaging;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalRemoteBridgeTest {

    @Test
    void validatesArguments() {
        LocalRemoteBridge bridge = new LocalRemoteBridge();

        assertThrows(NullPointerException.class, () -> bridge.subscribe(null, s -> {}));
        assertThrows(NullPointerException.class, () -> bridge.subscribe("boss.health", null));
        assertThrows(NullPointerException.class, () -> bridge.publish(null, "42"));
        assertThrows(NullPointerException.class, () -> bridge.publish("boss.health", null));
    }

    @Test
    void publishesPayloadToSubscribers() {
        LocalRemoteBridge bridge = new LocalRemoteBridge();
        AtomicReference<String> payload = new AtomicReference<>();

        bridge.subscribe("boss.health", payload::set);
        bridge.publish("boss.health", "42");

        assertEquals("42", payload.get());
    }

    @Test
    void continuesDeliveryWhenOneSubscriberFails() {
        LocalRemoteBridge bridge = new LocalRemoteBridge();
        AtomicInteger received = new AtomicInteger();

        bridge.subscribe("boss.health", ignored -> {
            throw new RuntimeException("boom");
        });
        bridge.subscribe("boss.health", ignored -> received.incrementAndGet());

        bridge.publish("boss.health", "42");

        assertEquals(1, received.get());
    }

    @Test
    void supportsUnsubscribe() {
        LocalRemoteBridge bridge = new LocalRemoteBridge();
        AtomicInteger received = new AtomicInteger();

        Consumer<String> consumer = ignored -> received.incrementAndGet();
        bridge.subscribe("boss.health", consumer);
        assertTrue(bridge.unsubscribe("boss.health", consumer));
        assertFalse(bridge.unsubscribe("boss.health", consumer));

        bridge.publish("boss.health", "42");
        assertEquals(0, received.get());
    }
}
