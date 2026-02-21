package io.github.fragmer2.bslib.api.di;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Lightweight service container / dependency injection.
 *
 * Register:
 *   container.register(Economy.class, new VaultEconomy());
 *   container.register(Database.class, "mysql", new MySQLDb());
 *   container.registerLazy(HeavyService.class, () -> new HeavyService());
 *
 * Retrieve:
 *   Economy eco = container.get(Economy.class);
 *   Database db = container.get(Database.class, "mysql");
 *
 * Inject into object:
 *   container.inject(myObject); // fills all @Inject fields
 */
public class ServiceContainer {
    private final Map<String, Object> services = new ConcurrentHashMap<>();
    private final Map<String, Supplier<?>> lazyServices = new ConcurrentHashMap<>();

    // ========== Registration ==========

    public <T> void register(Class<T> type, T instance) {
        services.put(key(type, ""), instance);
    }

    public <T> void register(Class<T> type, String name, T instance) {
        services.put(key(type, name), instance);
    }

    public <T> void registerLazy(Class<T> type, Supplier<T> supplier) {
        lazyServices.put(key(type, ""), supplier);
    }

    public <T> void registerLazy(Class<T> type, String name, Supplier<T> supplier) {
        lazyServices.put(key(type, name), supplier);
    }

    // ========== Retrieval ==========

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) resolve(key(type, ""));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, String name) {
        return (T) resolve(key(type, name));
    }

    public <T> T getOrDefault(Class<T> type, T defaultValue) {
        T result = get(type);
        return result != null ? result : defaultValue;
    }

    public boolean has(Class<?> type) {
        String k = key(type, "");
        return services.containsKey(k) || lazyServices.containsKey(k);
    }

    public boolean has(Class<?> type, String name) {
        String k = key(type, name);
        return services.containsKey(k) || lazyServices.containsKey(k);
    }

    // ========== Injection ==========

    /**
     * Injects all @Inject-annotated fields of the target object.
     */
    public void inject(Object target) {
        Class<?> clazz = target.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                Inject inject = field.getAnnotation(Inject.class);
                if (inject == null) continue;

                String name = inject.value();
                Object service = resolve(key(field.getType(), name));

                if (service == null) {
                    // Try to find by supertype/interface
                    service = findByType(field.getType());
                }

                if (service != null) {
                    field.setAccessible(true);
                    try {
                        field.set(target, service);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject " + field.getName() + " in " + clazz.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    // ========== Internal ==========

    private Object resolve(String key) {
        Object service = services.get(key);
        if (service != null) return service;

        Supplier<?> supplier = lazyServices.get(key);
        if (supplier != null) {
            service = supplier.get();
            services.put(key, service);
            lazyServices.remove(key);
            return service;
        }
        return null;
    }

    private Object findByType(Class<?> type) {
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            if (type.isInstance(entry.getValue())) {
                return entry.getValue();
            }
        }
        for (Map.Entry<String, Supplier<?>> entry : lazyServices.entrySet()) {
            // Can't check type without instantiating - skip lazy for supertype lookup
        }
        return null;
    }

    private String key(Class<?> type, String name) {
        return name.isEmpty() ? type.getName() : type.getName() + ":" + name;
    }

    /**
     * Clear all registered services.
     */
    public void clear() {
        services.clear();
        lazyServices.clear();
    }
}
