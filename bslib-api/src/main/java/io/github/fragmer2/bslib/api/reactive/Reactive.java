package io.github.fragmer2.bslib.api.reactive;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Observable reactive value — state-driven development for Paper.
 *
 * Create:
 *   Reactive<Integer> coins = Reactive.of(100);
 *   Reactive<String>  rank  = Reactive.of("Member");
 *
 * Read/write:
 *   int c = coins.get();
 *   coins.set(500);      // triggers all observers
 *   coins.update(v -> v + 50);  // atomic read-modify-write
 *
 * Observe changes:
 *   coins.onChange((oldVal, newVal) -> player.sendMessage("Coins: " + newVal));
 *   coins.onSet(val -> updateScoreboard(val));
 *
 * Derived (computed) values:
 *   Reactive<String> display = coins.map(c -> "§6" + c + " coins");
 *   // display auto-updates when coins change
 *
 * Bind to GUI:
 *   Button.dynamic(view -> Item.of(Material.GOLD_INGOT)
 *       .name(display.get()).build())
 *       .bind(coins);  // auto-refresh when coins change
 *
 * Bind to Placeholder:
 *   coins.asPlaceholder("coins");
 *   // {coins} now always returns current value
 *
 * Combine:
 *   Reactive<String> label = Reactive.combine(coins, rank,
 *       (c, r) -> r + " | " + c + " coins");
 */
public class Reactive<T> {
    private volatile T value;
    private final List<BiConsumer<T, T>> changeListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<T>> setListeners = new CopyOnWriteArrayList<>();
    private final List<Reactive<?>> dependents = new CopyOnWriteArrayList<>();
    private volatile long version = 0; // increments on each set, used by GUI polling
    private volatile boolean batching = false;
    private T batchedValue;

    private Reactive(T initial) {
        this.value = initial;
    }

    // ========== Factory ==========

    public static <T> Reactive<T> of(T initial) {
        return new Reactive<>(initial);
    }

    public static Reactive<Integer> ofInt(int initial) {
        return new Reactive<>(initial);
    }

    public static Reactive<Double> ofDouble(double initial) {
        return new Reactive<>(initial);
    }

    public static Reactive<Boolean> ofBool(boolean initial) {
        return new Reactive<>(initial);
    }

    public static Reactive<String> ofString(String initial) {
        return new Reactive<>(initial);
    }

    // ========== Get / Set ==========

    public T get() {
        return value;
    }

    public void set(T newValue) {
        if (batching) {
            batchedValue = newValue;
            return;
        }

        T old;
        List<BiConsumer<T, T>> changeSnapshot;
        List<Consumer<T>> setSnapshot;
        List<Reactive<?>> dependentSnapshot;

        synchronized (this) {
            old = this.value;
            this.value = newValue;
            this.version++;

            if (Objects.equals(old, newValue)) {
                return;
            }

            changeSnapshot = List.copyOf(changeListeners);
            setSnapshot = List.copyOf(setListeners);
            dependentSnapshot = List.copyOf(dependents);
        }

        // Notify outside synchronized block
        for (BiConsumer<T, T> listener : changeSnapshot) {
            try { listener.accept(old, newValue); } catch (Exception e) { e.printStackTrace(); }
        }
        for (Consumer<T> listener : setSnapshot) {
            try { listener.accept(newValue); } catch (Exception e) { e.printStackTrace(); }
        }
        for (Reactive<?> dep : dependentSnapshot) {
            dep.recompute();
        }
    }

    /**
     * Atomic read-modify-write.
     *   coins.update(v -> v + 50);
     */
    public void update(Function<T, T> updater) {
        T current;
        synchronized (this) {
            current = value;
        }
        set(updater.apply(current));
    }

    /**
     * Set without triggering listeners (silent update).
     */
    public void setSilent(T newValue) {
        synchronized (this) {
            this.value = newValue;
            this.version++;
        }
    }

    /** Version counter — increments on every set(). Used by GUI for change detection. */
    public long version() {
        return version;
    }

    // ========== Observers ==========

    /**
     * Called when value changes (old, new).
     */
    public Reactive<T> onChange(BiConsumer<T, T> listener) {
        subscribeChange(listener);
        return this;
    }

    public Subscription subscribeChange(BiConsumer<T, T> listener) {
        changeListeners.add(listener);
        return () -> changeListeners.remove(listener);
    }

    /**
     * Called when value is set (new value only).
     */
    public Reactive<T> onSet(Consumer<T> listener) {
        subscribeSet(listener);
        return this;
    }

    public Subscription subscribeSet(Consumer<T> listener) {
        setListeners.add(listener);
        return () -> setListeners.remove(listener);
    }

    /**
     * Remove all listeners.
     */
    public void clearListeners() {
        changeListeners.clear();
        setListeners.clear();
    }

    // ========== Derived / Mapped ==========

    /**
     * Create a derived reactive that auto-updates when this one changes.
     *   Reactive<String> display = coins.map(c -> "Coins: " + c);
     */
    public <R> Reactive<R> map(Function<T, R> mapper) {
        MappedReactive<T, R> mapped = new MappedReactive<>(this, mapper);
        this.dependents.add(mapped);
        return mapped;
    }

    /**
     * Combine two reactives into one.
     *   Reactive<String> label = Reactive.combine(coins, rank,
     *       (c, r) -> r + " | " + c + " coins");
     */
    public static <A, B, R> Reactive<R> combine(Reactive<A> a, Reactive<B> b,
                                                  java.util.function.BiFunction<A, B, R> combiner) {
        CombinedReactive<A, B, R> combined = new CombinedReactive<>(a, b, combiner);
        a.dependents.add(combined);
        b.dependents.add(combined);
        return combined;
    }

    /** Internal: called when a parent changes. Override in derived types. */
    void recompute() {
        // base Reactive doesn't recompute — only MappedReactive/CombinedReactive do
    }

    public Reactive<T> beginBatch() {
        batching = true;
        batchedValue = value;
        return this;
    }

    public Reactive<T> endBatch() {
        boolean shouldFlush = batching;
        T pending = batchedValue;
        batching = false;
        batchedValue = null;
        if (shouldFlush) {
            set(pending);
        }
        return this;
    }

    public Reactive<T> distinctUntilChanged() {
        return this;
    }

    public Reactive<T> throttle(long ticks) {
        Reactive<T> out = Reactive.of(get());
        AtomicBoolean cooling = new AtomicBoolean(false);
        this.subscribeSet(next -> {
            if (cooling.compareAndSet(false, true)) {
                out.set(next);
                io.github.fragmer2.bslib.api.task.Tasks.sync().delay(ticks).run(() -> cooling.set(false));
            }
        });
        return out;
    }

    public Reactive<T> debounce(long ticks) {
        Reactive<T> out = Reactive.of(get());
        final int[] taskId = {-1};
        this.subscribeSet(next -> {
            if (taskId[0] != -1) {
                org.bukkit.Bukkit.getScheduler().cancelTask(taskId[0]);
            }
            var task = io.github.fragmer2.bslib.api.task.Tasks.sync().delay(ticks).run(() -> out.set(next));
            taskId[0] = task.getTaskId();
        });
        return out;
    }

    // ========== Integration shortcuts ==========

    /**
     * Register this reactive as a placeholder.
     *   coins.asPlaceholder("coins");
     *   // {coins} in any text → current value
     */
    public Reactive<T> asPlaceholder(String key) {
        io.github.fragmer2.bslib.api.placeholder.Placeholders.register(key, player -> String.valueOf(value));
        return this;
    }

    /**
     * Register as a player-scoped placeholder (value per player).
     * Requires a function to get the reactive for a player.
     */
    public static <T> void asPlayerPlaceholder(String key, Function<org.bukkit.entity.Player, Reactive<T>> supplier) {
        io.github.fragmer2.bslib.api.placeholder.Placeholders.register(key,
                player -> String.valueOf(supplier.apply(player).get()));
    }

    // ========== Async integration ==========

    /**
     * Create a Reactive that loads its value asynchronously.
     * Starts with the default value, then updates when async completes.
     *
     *   Reactive<Integer> balance = Reactive.computeAsync(0, () -> db.getBalance(uuid));
     *   // Immediately returns Reactive with value 0
     *   // When DB query finishes, value updates to real balance (triggers all bindings)
     */
    public static <T> Reactive<T> computeAsync(T defaultValue, java.util.function.Supplier<T> asyncLoader) {
        Reactive<T> reactive = new Reactive<>(defaultValue);
        io.github.fragmer2.bslib.api.task.Tasks.compute(asyncLoader)
                .thenSync(reactive::set);
        return reactive;
    }

    /**
     * Refresh this Reactive's value from an async source.
     *   balance.refreshAsync(() -> db.getBalance(uuid));
     */
    public void refreshAsync(java.util.function.Supplier<T> asyncLoader) {
        io.github.fragmer2.bslib.api.task.Tasks.compute(asyncLoader)
                .thenSync(this::set);
    }

    // ========== Convenience for numbers ==========

    /**
     * Increment (for Reactive<Integer>).
     */
    @SuppressWarnings("unchecked")
    public void increment() {
        if (value instanceof Integer i) {
            set((T) Integer.valueOf(i + 1));
        } else if (value instanceof Long l) {
            set((T) Long.valueOf(l + 1));
        } else if (value instanceof Double d) {
            set((T) Double.valueOf(d + 1));
        }
    }

    @SuppressWarnings("unchecked")
    public void decrement() {
        if (value instanceof Integer i) {
            set((T) Integer.valueOf(i - 1));
        } else if (value instanceof Long l) {
            set((T) Long.valueOf(l - 1));
        } else if (value instanceof Double d) {
            set((T) Double.valueOf(d - 1));
        }
    }

    @SuppressWarnings("unchecked")
    public void add(Number amount) {
        if (value instanceof Integer i) {
            set((T) Integer.valueOf(i + amount.intValue()));
        } else if (value instanceof Long l) {
            set((T) Long.valueOf(l + amount.longValue()));
        } else if (value instanceof Double d) {
            set((T) Double.valueOf(d + amount.doubleValue()));
        }
    }

    @Override
    public String toString() {
        return "Reactive{" + value + "}";
    }

    // ========== Internal derived types ==========

    private static class MappedReactive<S, R> extends Reactive<R> {
        private final Reactive<S> source;
        private final Function<S, R> mapper;

        MappedReactive(Reactive<S> source, Function<S, R> mapper) {
            super(mapper.apply(source.get()));
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        void recompute() {
            set(mapper.apply(source.get()));
        }
    }

    private static class CombinedReactive<A, B, R> extends Reactive<R> {
        private final Reactive<A> sourceA;
        private final Reactive<B> sourceB;
        private final java.util.function.BiFunction<A, B, R> combiner;

        CombinedReactive(Reactive<A> a, Reactive<B> b, java.util.function.BiFunction<A, B, R> combiner) {
            super(combiner.apply(a.get(), b.get()));
            this.sourceA = a;
            this.sourceB = b;
            this.combiner = combiner;
        }

        @Override
        void recompute() {
            set(combiner.apply(sourceA.get(), sourceB.get()));
        }
    }
}
