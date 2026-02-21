package io.github.fragmer2.bslib.api.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Lightweight event bus for inter-plugin communication.
 *
 * Publish:
 *   Bus.publish(new PlayerLevelUp(player, 10));
 *
 * Subscribe:
 *   Bus.subscribe(PlayerLevelUp.class, event -> {
 *       event.player().sendMessage("Level up!");
 *   });
 *
 * Scoped (auto-cleanup):
 *   Bus.subscribe(PlayerLevelUp.class, event -> { ... }, myPlugin);
 *   Bus.unsubscribeAll(myPlugin);
 *
 * Any class can be an event — no interface needed.
 */
public final class Bus {
    private static final Map<Class<?>, List<Subscription<?>>> subscriptions = new ConcurrentHashMap<>();

    private Bus() {}

    // ========== Subscribe ==========

    /**
     * Subscribe to an event type.
     */
    public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler) {
        return subscribe(eventType, handler, null);
    }

    /**
     * Subscribe with an owner (for bulk unsubscribe).
     */
    public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler, Object owner) {
        Subscription<T> sub = new Subscription<>(eventType, handler, owner);
        subscriptions.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(sub);
        return sub;
    }

    // ========== Publish ==========

    /**
     * Publish an event to all subscribers.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T publish(T event) {
        List<Subscription<?>> subs = subscriptions.get(event.getClass());
        if (subs != null) {
            for (Subscription sub : subs) {
                if (sub.active) {
                    try {
                        sub.handler.accept(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return event; // return for chaining / inspection
    }

    // ========== Unsubscribe ==========

    /**
     * Unsubscribe a specific subscription.
     */
    public static void unsubscribe(Subscription<?> sub) {
        sub.active = false;
        List<Subscription<?>> subs = subscriptions.get(sub.eventType);
        if (subs != null) subs.remove(sub);
    }

    /**
     * Unsubscribe all subscriptions owned by an object.
     * Call on plugin disable for cleanup.
     */
    public static void unsubscribeAll(Object owner) {
        for (List<Subscription<?>> subs : subscriptions.values()) {
            subs.removeIf(s -> s.owner == owner);
        }
    }

    /** Clear everything (called on BSLib disable). */
    public static void clear() {
        subscriptions.clear();
    }

    // ========== Subscription handle ==========

    public static class Subscription<T> {
        final Class<T> eventType;
        final Consumer<T> handler;
        final Object owner;
        volatile boolean active = true;

        Subscription(Class<T> eventType, Consumer<T> handler, Object owner) {
            this.eventType = eventType;
            this.handler = handler;
            this.owner = owner;
        }

        public void cancel() {
            Bus.unsubscribe(this);
        }
    }
}
