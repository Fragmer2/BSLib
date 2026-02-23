package io.github.fragmer2.bslib.api.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Global service registry — the ecosystem backbone.
 * Plugins provide and consume services through this API.
 *
 * Provider plugin:
 *   Services.provide(Economy.class, new VaultEconomy());
 *
 * Consumer plugin:
 *   Economy eco = Services.get(Economy.class);
 *   if (Services.has(Economy.class)) { ... }
 *
 * Named services:
 *   Services.provide(Database.class, "mysql", new MySQLDb());
 *   Database db = Services.get(Database.class, "mysql");
 *
 * Lazy (created on first access):
 *   Services.provideLazy(HeavyService.class, HeavyService::new);
 *
 * Listen for service registration:
 *   Services.onProvide(Economy.class, eco -> setupShop(eco));
 */
public final class Services {
    private static final Map<String, Object> services = new ConcurrentHashMap<>();
    private static final Map<String, Supplier<?>> lazy = new ConcurrentHashMap<>();
    private static final Map<String, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    private Services() {}

    // ========== Provide ==========

    public static <T> void provide(Class<T> type, T instance) {
        provide(type, "", instance);
    }

    public static <T> void provide(Class<T> type, String name, T instance) {
        String k = key(type, name);
        services.put(k, instance);
        notifyListeners(k, instance);
        // Bridge: also register in internal DI container for @Inject support
        try {
            io.github.fragmer2.bslib.api.BSLib.getContainer().register(type, instance);
        } catch (Exception ignored) {
            // Container may not be initialized yet
        }
    }

    public static <T> void provideLazy(Class<T> type, Supplier<T> supplier) {
        lazy.put(key(type, ""), supplier);
    }

    public static <T> void provideLazy(Class<T> type, String name, Supplier<T> supplier) {
        lazy.put(key(type, name), supplier);
    }

    // ========== Consume ==========

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        return (T) resolve(key(type, ""));
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type, String name) {
        return (T) resolve(key(type, name));
    }

    public static <T> T getOrDefault(Class<T> type, T fallback) {
        T result = get(type);
        return result != null ? result : fallback;
    }

    public static boolean has(Class<?> type) {
        String k = key(type, "");
        return services.containsKey(k) || lazy.containsKey(k);
    }

    public static boolean has(Class<?> type, String name) {
        String k = key(type, name);
        return services.containsKey(k) || lazy.containsKey(k);
    }

    public static int registeredCount() {
        return services.size();
    }

    public static int lazyCount() {
        return lazy.size();
    }

    public static Set<String> serviceKeys() {
        return new LinkedHashSet<>(services.keySet());
    }

    // ========== Listen for registration ==========

    @SuppressWarnings("unchecked")
    public static <T> void onProvide(Class<T> type, Consumer<T> callback) {
        String k = key(type, "");
        // If already provided, fire immediately
        Object existing = resolve(k);
        if (existing != null) {
            callback.accept((T) existing);
            return;
        }
        listeners.computeIfAbsent(k, x -> new CopyOnWriteArrayList<>()).add(callback);
    }

    // ========== Remove ==========

    public static void remove(Class<?> type) {
        services.remove(key(type, ""));
        lazy.remove(key(type, ""));
    }

    public static void remove(Class<?> type, String name) {
        services.remove(key(type, name));
        lazy.remove(key(type, name));
    }

    /** Clear all services (called on BSLib disable). */
    public static void clear() {
        services.clear();
        lazy.clear();
        listeners.clear();
    }

    // ========== Internal ==========

    private static Object resolve(String k) {
        Object s = services.get(k);
        if (s != null) return s;
        Supplier<?> sup = lazy.remove(k);
        if (sup != null) {
            s = sup.get();
            services.put(k, s);
            notifyListeners(k, s);
            return s;
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void notifyListeners(String k, Object instance) {
        List<Consumer<?>> list = listeners.remove(k);
        if (list != null) {
            for (Consumer c : list) {
                c.accept(instance);
            }
        }
    }

    private static String key(Class<?> type, String name) {
        return name.isEmpty() ? type.getName() : type.getName() + ":" + name;
    }
}
