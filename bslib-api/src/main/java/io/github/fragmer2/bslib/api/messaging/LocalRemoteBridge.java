package io.github.fragmer2.bslib.api.messaging;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * In-memory RemoteBridge implementation useful for development and single-server setups.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Reference bridge implementation")
public final class LocalRemoteBridge implements RemoteBridge {
    private final Map<String, List<Consumer<String>>> consumers = new ConcurrentHashMap<>();

    @Override
    public void publish(String channel, String payload) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(payload, "payload");
        for (Consumer<String> consumer : consumers.getOrDefault(channel, List.of())) {
            try {
                consumer.accept(payload);
            } catch (Throwable ignored) {
                // isolate broken subscribers to keep delivery for others
            }
        }
    }

    @Override
    public void subscribe(String channel, Consumer<String> consumer) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(consumer, "consumer");
        consumers.computeIfAbsent(channel, ignored -> new CopyOnWriteArrayList<>()).add(consumer);
    }

    public boolean unsubscribe(String channel, Consumer<String> consumer) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(consumer, "consumer");
        List<Consumer<String>> channelConsumers = consumers.get(channel);
        return channelConsumers != null && channelConsumers.remove(consumer);
    }

    public void clearChannel(String channel) {
        Objects.requireNonNull(channel, "channel");
        consumers.remove(channel);
    }
}
