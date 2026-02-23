package io.github.fragmer2.bslib.api.reactive;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Observable reactive value — state-driven development for Paper.
 */
public class Reactive<T> {
    private static final Object NULL_SENTINEL = new Object();

    private volatile T value;
    private final List<BiConsumer<T, T>> changeListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<T>> setListeners = new CopyOnWriteArrayList<>();
    private final List<Reactive<?>> dependents = new CopyOnWriteArrayList<>();
    private final List<Subscription> upstreamSubscriptions = new CopyOnWriteArrayList<>();
    private final List<Runnable> destroyHooks = new CopyOnWriteArrayList<>();

    private volatile long version = 0;
    private volatile boolean batching = false;
    private T batchedValue;
    private volatile boolean destroyed = false;

    private Reactive(T initial) {
        this.value = initial;
    }

    public static <T> Reactive<T> of(T initial) {
        return new Reactive<>(initial);
    }

    public static Reactive<Integer> ofInt(int initial) { return new Reactive<>(initial); }
    public static Reactive<Double> ofDouble(double initial) { return new Reactive<>(initial); }
    public static Reactive<Boolean> ofBool(boolean initial) { return new Reactive<>(initial); }
    public static Reactive<String> ofString(String initial) { return new Reactive<>(initial); }

    public T get() { return value; }

    public void set(T newValue) {
        ChangeDispatch<T> dispatch;
        synchronized (this) {
            dispatch = prepareSetLocked(newValue, true);
        }
        dispatchOutsideLock(dispatch);
    }

    public void update(Function<T, T> updater) {
        ChangeDispatch<T> dispatch;
        synchronized (this) {
            dispatch = prepareSetLocked(updater.apply(value), true);
        }
        dispatchOutsideLock(dispatch);
    }

    public void setSilent(T newValue) {
        synchronized (this) {
            if (destroyed) return;
            value = newValue;
            version++;
            if (batching) {
                batchedValue = newValue;
            }
        }
    }

    public synchronized Reactive<T> beginBatch() {
        if (destroyed) return this;
        batching = true;
        batchedValue = value;
        return this;
    }

    public Reactive<T> endBatch() {
        ChangeDispatch<T> dispatch = null;
        synchronized (this) {
            if (destroyed) return this;
            if (batching) {
                T pending = batchedValue;
                batching = false;
                batchedValue = null;
                dispatch = prepareSetLocked(pending, false);
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
        dispatchOutsideLock(dispatch);
        return this;
    }

    public long version() { return version; }

    public Reactive<T> onChange(BiConsumer<T, T> listener) {
        subscribeChange(listener);
        return this;
    }

    public Subscription subscribeChange(BiConsumer<T, T> listener) {
        if (destroyed) return () -> {};
        changeListeners.add(listener);
        return () -> changeListeners.remove(listener);
    }

    public Reactive<T> onSet(Consumer<T> listener) {
        subscribeSet(listener);
        return this;
    }

    public Subscription subscribeSet(Consumer<T> listener) {
        if (destroyed) return () -> {};
        setListeners.add(listener);
        return () -> setListeners.remove(listener);
    }

    public void clearListeners() {
        changeListeners.clear();
        setListeners.clear();
    }

    public <R> Reactive<R> map(Function<T, R> mapper) {
        MappedReactive<T, R> mapped = new MappedReactive<>(this, mapper);
        this.dependents.add(mapped);
        mapped.registerDestroyHook(() -> this.dependents.remove(mapped));
        return mapped;
    }

    public static <A, B, R> Reactive<R> combine(Reactive<A> a, Reactive<B> b,
                                                java.util.function.BiFunction<A, B, R> combiner) {
        CombinedReactive<A, B, R> combined = new CombinedReactive<>(a, b, combiner);
        a.dependents.add(combined);
        b.dependents.add(combined);
        combined.registerDestroyHook(() -> a.dependents.remove(combined));
        combined.registerDestroyHook(() -> b.dependents.remove(combined));
        return combined;
    }

    public Reactive<T> distinctUntilChanged() {
        Reactive<T> out = Reactive.of(get());
        Object[] last = new Object[] { get() == null ? NULL_SENTINEL : get() };

        Subscription upstream = this.subscribeSet(next -> {
            Object marker = next == null ? NULL_SENTINEL : next;
            if (!Objects.equals(last[0], marker)) {
                last[0] = marker;
                out.set(next);
            }
        });

        out.attachUpstream(upstream);
        return out;
    }

    public Reactive<T> throttle(long ticks) {
        Reactive<T> out = Reactive.of(get());
        AtomicBoolean cooling = new AtomicBoolean(false);

        Subscription upstream = this.subscribeSet(next -> {
            if (cooling.compareAndSet(false, true)) {
                out.set(next);
                var task = io.github.fragmer2.bslib.api.task.Tasks.sync().delay(ticks).run(() -> cooling.set(false));
                out.registerDestroyHook(task::cancel);
            }
        });

        out.attachUpstream(upstream);
        return out;
    }

    public Reactive<T> debounce(long ticks) {
        Reactive<T> out = Reactive.of(get());
        AtomicInteger taskId = new AtomicInteger(-1);

        Subscription upstream = this.subscribeSet(next -> {
            int prev = taskId.getAndSet(-1);
            if (prev != -1) {
                org.bukkit.Bukkit.getScheduler().cancelTask(prev);
            }
            var task = io.github.fragmer2.bslib.api.task.Tasks.sync().delay(ticks).run(() -> out.set(next));
            taskId.set(task.getTaskId());
        });

        out.attachUpstream(upstream);
        out.registerDestroyHook(() -> {
            int id = taskId.getAndSet(-1);
            if (id != -1) {
                org.bukkit.Bukkit.getScheduler().cancelTask(id);
            }
        });

        return out;
    }

    void recompute() {
        // only derived types recompute
    }

    public void destroy() {
        if (destroyed) return;
        destroyed = true;

        List<Reactive<?>> dependentSnapshot = List.copyOf(dependents);
        dependentSnapshot.forEach(Reactive::destroy);

        clearListeners();
        upstreamSubscriptions.forEach(Subscription::unsubscribe);
        upstreamSubscriptions.clear();

        destroyHooks.forEach(h -> {
            try { h.run(); } catch (Exception ignored) {}
        });
        destroyHooks.clear();
        dependents.clear();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Reactive<T> asPlaceholder(String key) {
        io.github.fragmer2.bslib.api.placeholder.Placeholders.register(key, player -> String.valueOf(value));
        return this;
    }

    public static <T> void asPlayerPlaceholder(String key, Function<org.bukkit.entity.Player, Reactive<T>> supplier) {
        io.github.fragmer2.bslib.api.placeholder.Placeholders.register(key,
                player -> String.valueOf(supplier.apply(player).get()));
    }

    public static <T> Reactive<T> computeAsync(T defaultValue, java.util.function.Supplier<T> asyncLoader) {
        Reactive<T> reactive = new Reactive<>(defaultValue);
        io.github.fragmer2.bslib.api.task.Tasks.compute(asyncLoader).thenSync(reactive::set);
        return reactive;
    }

    public void refreshAsync(java.util.function.Supplier<T> asyncLoader) {
        io.github.fragmer2.bslib.api.task.Tasks.compute(asyncLoader).thenSync(this::set);
    }

    @SuppressWarnings("unchecked")
    public void increment() {
        if (value instanceof Integer i) set((T) Integer.valueOf(i + 1));
        else if (value instanceof Long l) set((T) Long.valueOf(l + 1));
        else if (value instanceof Double d) set((T) Double.valueOf(d + 1));
    }

    @SuppressWarnings("unchecked")
    public void decrement() {
        if (value instanceof Integer i) set((T) Integer.valueOf(i - 1));
        else if (value instanceof Long l) set((T) Long.valueOf(l - 1));
        else if (value instanceof Double d) set((T) Double.valueOf(d - 1));
    }

    @SuppressWarnings("unchecked")
    public void add(Number amount) {
        if (value instanceof Integer i) set((T) Integer.valueOf(i + amount.intValue()));
        else if (value instanceof Long l) set((T) Long.valueOf(l + amount.longValue()));
        else if (value instanceof Double d) set((T) Double.valueOf(d + amount.doubleValue()));
    }

    @Override
    public String toString() { return "Reactive{" + value + "}"; }

    private synchronized ChangeDispatch<T> prepareSetLocked(T newValue, boolean honorBatching) {
        if (destroyed) return null;

        if (honorBatching && batching) {
            batchedValue = newValue;
            return null;
        }

        T old = this.value;
        this.value = newValue;
        this.version++;

        if (Objects.equals(old, newValue)) {
            return null;
        }

        return new ChangeDispatch<>(old, newValue,
                List.copyOf(changeListeners),
                List.copyOf(setListeners),
                List.copyOf(dependents));
    }

    private void dispatchOutsideLock(ChangeDispatch<T> dispatch) {
        if (dispatch == null || destroyed) return;

        for (BiConsumer<T, T> listener : dispatch.changeSnapshot()) {
            try { listener.accept(dispatch.oldValue(), dispatch.newValue()); } catch (Exception e) { e.printStackTrace(); }
        }
        for (Consumer<T> listener : dispatch.setSnapshot()) {
            try { listener.accept(dispatch.newValue()); } catch (Exception e) { e.printStackTrace(); }
        }
        for (Reactive<?> dep : dispatch.dependentSnapshot()) {
            dep.recompute();
        }
    }

    protected void attachUpstream(Subscription subscription) {
        if (subscription != null) {
            upstreamSubscriptions.add(subscription);
            registerDestroyHook(subscription::unsubscribe);
        }
    }

    protected void registerDestroyHook(Runnable hook) {
        if (hook != null) destroyHooks.add(hook);
    }

    private record ChangeDispatch<T>(T oldValue, T newValue,
                                     List<BiConsumer<T, T>> changeSnapshot,
                                     List<Consumer<T>> setSnapshot,
                                     List<Reactive<?>> dependentSnapshot) {}

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
