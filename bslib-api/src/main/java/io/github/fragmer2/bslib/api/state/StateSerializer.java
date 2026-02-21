package io.github.fragmer2.bslib.api.state;

import io.github.fragmer2.bslib.api.reactive.Reactive;
import io.github.fragmer2.bslib.api.reactive.ReactiveList;
import io.github.fragmer2.bslib.api.reactive.ReactiveMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Serializes @State objects to/from Map<String, Object> for storage.
 * Handles: Reactive, ReactiveList, ReactiveMap, primitives, String, UUID, List, Map.
 */
public final class StateSerializer {

    private StateSerializer() {}

    /**
     * Serialize a @State object to a flat map.
     */
    public static Map<String, Object> serialize(Object state) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field field : getSerializableFields(state.getClass())) {
            field.setAccessible(true);
            try {
                Object value = field.get(state);
                if (value == null) continue;

                String key = field.getName();

                if (value instanceof Reactive<?> r) {
                    map.put(key, toSerializable(r.get()));
                } else if (value instanceof ReactiveList<?> rl) {
                    map.put(key, new ArrayList<>(rl.asList()));
                } else if (value instanceof ReactiveMap<?, ?> rm) {
                    map.put(key, new LinkedHashMap<>(rm.asMap()));
                } else {
                    map.put(key, toSerializable(value));
                }
            } catch (Exception e) {
                // Skip fields that can't be serialized
            }
        }
        return map;
    }

    /**
     * Deserialize a map into a @State object.
     * Updates existing Reactive fields in-place (triggers listeners).
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void deserialize(Object state, Map<String, Object> data) {
        for (Field field : getSerializableFields(state.getClass())) {
            field.setAccessible(true);
            String key = field.getName();
            Object stored = data.get(key);
            if (stored == null) continue;

            try {
                Object current = field.get(state);

                if (current instanceof Reactive r) {
                    // Update Reactive in-place (triggers listeners!)
                    r.setSilent(convertValue(stored, guessReactiveType(r)));
                } else if (current instanceof ReactiveList rl) {
                    if (stored instanceof List list) {
                        rl.replaceAll(list);
                    }
                } else if (current instanceof ReactiveMap rm) {
                    if (stored instanceof Map map) {
                        rm.clear();
                        rm.putAll(map);
                    }
                } else {
                    // Direct field set
                    field.set(state, convertValue(stored, field.getType()));
                }
            } catch (Exception e) {
                // Skip fields that fail
            }
        }
    }

    /**
     * Get the key value from a @State object.
     */
    public static String getKey(Object state) {
        for (Field field : state.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(StateKey.class)) {
                field.setAccessible(true);
                try {
                    Object val = field.get(state);
                    return val != null ? val.toString() : null;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Set the key value on a @State object.
     */
    public static void setKey(Object state, String keyValue) {
        for (Field field : state.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(StateKey.class)) {
                field.setAccessible(true);
                try {
                    if (field.getType() == UUID.class) {
                        field.set(state, UUID.fromString(keyValue));
                    } else if (field.getType() == String.class) {
                        field.set(state, keyValue);
                    } else {
                        field.set(state, keyValue);
                    }
                } catch (Exception e) {
                    // ignore
                }
                return;
            }
        }
    }

    // ========== Internal ==========

    private static List<Field> getSerializableFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (Modifier.isTransient(field.getModifiers())) continue;
            if (field.isAnnotationPresent(StateKey.class)) continue; // key handled separately
            fields.add(field);
        }
        return fields;
    }

    private static Object toSerializable(Object value) {
        if (value == null) return null;
        if (value instanceof UUID uuid) return uuid.toString();
        if (value instanceof Enum<?> e) return e.name();
        // Primitives, String, Number, Boolean, List, Map — already serializable
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Object convertValue(Object stored, Class<?> targetType) {
        if (stored == null) return null;
        if (targetType == null) return stored;

        // Direct match
        if (targetType.isInstance(stored)) return stored;

        // Number conversions
        if (stored instanceof Number num) {
            if (targetType == int.class || targetType == Integer.class) return num.intValue();
            if (targetType == long.class || targetType == Long.class) return num.longValue();
            if (targetType == double.class || targetType == Double.class) return num.doubleValue();
            if (targetType == float.class || targetType == Float.class) return num.floatValue();
        }

        // String conversions
        if (stored instanceof String s) {
            if (targetType == UUID.class) return UUID.fromString(s);
            if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(s);
            if (targetType == long.class || targetType == Long.class) return Long.parseLong(s);
            if (targetType == double.class || targetType == Double.class) return Double.parseDouble(s);
            if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(s);
        }

        // Boolean from various types
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (stored instanceof Boolean b) return b;
            return Boolean.parseBoolean(stored.toString());
        }

        return stored;
    }

    /**
     * Try to guess the inner type of a Reactive from its current value.
     */
    private static Class<?> guessReactiveType(Reactive<?> reactive) {
        Object val = reactive.get();
        return val != null ? val.getClass() : null;
    }
}
