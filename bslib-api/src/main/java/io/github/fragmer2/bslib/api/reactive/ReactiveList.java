package io.github.fragmer2.bslib.api.reactive;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Observable list — fires listeners on any mutation.
 *
 * Usage:
 *   ReactiveList<String> friends = ReactiveList.of("Steve", "Alex");
 *   friends.onChange(list -> updateScoreboard(list));
 *   friends.add("Notch");  // → scoreboard updates
 *   friends.remove("Alex"); // → scoreboard updates
 *
 * Bind to GUI:
 *   // Paginated friend list in menu
 *   for (int i = 0; i < 9; i++) {
 *       int idx = i;
 *       menu.setButton(i, Button.dynamic(view -> {
 *           if (idx < friends.size()) return Item.of(Material.PLAYER_HEAD).name(friends.get(idx)).build();
 *           return Item.of(Material.AIR).build();
 *       }).bind(friends));
 *   }
 */
public class ReactiveList<T> {
    private final List<T> list;
    private final List<Consumer<List<T>>> listeners = new CopyOnWriteArrayList<>();
    private volatile long version = 0;

    private ReactiveList(List<T> initial) {
        this.list = new CopyOnWriteArrayList<>(initial);
    }

    @SafeVarargs
    public static <T> ReactiveList<T> of(T... items) {
        return new ReactiveList<>(Arrays.asList(items));
    }

    public static <T> ReactiveList<T> of(Collection<T> items) {
        return new ReactiveList<>(new ArrayList<>(items));
    }

    public static <T> ReactiveList<T> empty() {
        return new ReactiveList<>(Collections.emptyList());
    }

    // ========== Mutate (all fire listeners) ==========

    public void add(T item) {
        list.add(item);
        fire();
    }

    public void add(int index, T item) {
        list.add(index, item);
        fire();
    }

    public boolean remove(T item) {
        boolean removed = list.remove(item);
        if (removed) fire();
        return removed;
    }

    public T remove(int index) {
        T removed = list.remove(index);
        fire();
        return removed;
    }

    public void set(int index, T item) {
        list.set(index, item);
        fire();
    }

    public void clear() {
        list.clear();
        fire();
    }

    public void addAll(Collection<T> items) {
        list.addAll(items);
        fire();
    }

    public void replaceAll(Collection<T> items) {
        list.clear();
        list.addAll(items);
        fire();
    }

    public void sort(Comparator<? super T> comparator) {
        list.sort(comparator);
        fire();
    }

    // ========== Read ==========

    public T get(int index) { return list.get(index); }
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public boolean contains(T item) { return list.contains(item); }
    public int indexOf(T item) { return list.indexOf(item); }
    public List<T> asList() { return Collections.unmodifiableList(list); }
    public long version() { return version; }

    // ========== Observe ==========

    public ReactiveList<T> onChange(Consumer<List<T>> listener) {
        listeners.add(listener);
        return this;
    }

    /** For Button.bind() — returns a Supplier that tracks version. */
    public java.util.function.Supplier<Object> asBindable() {
        return this::version;
    }

    private void fire() {
        version++;
        List<T> snapshot = Collections.unmodifiableList(new ArrayList<>(list));
        for (Consumer<List<T>> listener : listeners) {
            try { listener.accept(snapshot); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public String toString() {
        return "ReactiveList" + list;
    }
}
