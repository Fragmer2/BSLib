package io.github.fragmer2.bslib.api.reactive;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Observable map — fires listeners on any mutation.
 *
 *   ReactiveMap<String, Integer> stats = ReactiveMap.empty();
 *   stats.onChange((k, v) -> updateDisplay());
 *   stats.put("kills", 10);  // → triggers
 *   stats.put("deaths", 3);  // → triggers
 */
public class ReactiveMap<K, V> {
    private final Map<K, V> map;
    private final List<Consumer<Map<K, V>>> mapListeners = new CopyOnWriteArrayList<>();
    private final List<BiConsumer<K, V>> entryListeners = new CopyOnWriteArrayList<>();
    private volatile long version = 0;

    private ReactiveMap(Map<K, V> initial) {
        this.map = new ConcurrentHashMap<>(initial);
    }

    public static <K, V> ReactiveMap<K, V> empty() {
        return new ReactiveMap<>(Collections.emptyMap());
    }

    public static <K, V> ReactiveMap<K, V> of(Map<K, V> initial) {
        return new ReactiveMap<>(initial);
    }

    // ========== Mutate ==========

    public V put(K key, V value) {
        V old = map.put(key, value);
        fire(key, value);
        return old;
    }

    public V remove(K key) {
        V old = map.remove(key);
        if (old != null) fire(key, null);
        return old;
    }

    public void putAll(Map<K, V> entries) {
        map.putAll(entries);
        version++;
        for (Consumer<Map<K, V>> listener : mapListeners) {
            try { listener.accept(asMap()); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void clear() {
        map.clear();
        version++;
        for (Consumer<Map<K, V>> listener : mapListeners) {
            try { listener.accept(asMap()); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // ========== Read ==========

    public V get(K key) { return map.get(key); }
    public V getOrDefault(K key, V def) { return map.getOrDefault(key, def); }
    public boolean containsKey(K key) { return map.containsKey(key); }
    public int size() { return map.size(); }
    public boolean isEmpty() { return map.isEmpty(); }
    public Map<K, V> asMap() { return Collections.unmodifiableMap(map); }
    public long version() { return version; }

    // ========== Observe ==========

    /** Full map change listener. */
    public ReactiveMap<K, V> onChange(Consumer<Map<K, V>> listener) {
        mapListeners.add(listener);
        return this;
    }

    /** Per-entry listener (key, newValue). newValue is null on remove. */
    public ReactiveMap<K, V> onChange(BiConsumer<K, V> listener) {
        entryListeners.add(listener);
        return this;
    }

    public java.util.function.Supplier<Object> asBindable() {
        return this::version;
    }

    private void fire(K key, V value) {
        version++;
        for (BiConsumer<K, V> listener : entryListeners) {
            try { listener.accept(key, value); } catch (Exception e) { e.printStackTrace(); }
        }
        Map<K, V> snapshot = asMap();
        for (Consumer<Map<K, V>> listener : mapListeners) {
            try { listener.accept(snapshot); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
