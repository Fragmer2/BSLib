package io.github.fragmer2.bslib.api.messaging;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Lightweight local cross-plugin messaging bridge.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Topic contracts may evolve")
public final class PluginMessageBus {
    private static final Map<String, List<Consumer<Object>>> subscribers = new ConcurrentHashMap<>();

    private PluginMessageBus() {}

    public static <T> void subscribe(String topic, Consumer<T> consumer) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add((Consumer<Object>) consumer);
    }

    public static void publish(String topic, Object payload) {
        for (Consumer<Object> consumer : subscribers.getOrDefault(topic, List.of())) {
            consumer.accept(payload);
        }
    }

    public static void clearTopic(String topic) {
        subscribers.remove(topic);
    }

    public static void clearAll() {
        subscribers.clear();
    }
}
