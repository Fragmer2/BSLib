package io.github.fragmer2.bslib.api.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-player session data container.
 *
 * Usage:
 *   Session session = Sessions.of(player);
 *   session.set("coins", 100);
 *   int coins = session.getInt("coins");
 *   session.set("vip", true);
 *   boolean vip = session.getBool("vip");
 */
public class Session {
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    // ========== Set ==========

    public Session set(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Session remove(String key) {
        data.remove(key);
        return this;
    }

    public void clear() {
        data.clear();
    }

    // ========== Get ==========

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get(key);
    }

    public <T> T get(String key, T defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        try {
            @SuppressWarnings("unchecked")
            T cast = (T) val;
            return cast;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String def) {
        Object val = data.get(key);
        return val != null ? val.toString() : def;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        Object val = data.get(key);
        if (val instanceof Number n) return n.intValue();
        if (val instanceof String s) {
            try { return Integer.parseInt(s); } catch (Exception e) { return def; }
        }
        return def;
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double def) {
        Object val = data.get(key);
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof String s) {
            try { return Double.parseDouble(s); } catch (Exception e) { return def; }
        }
        return def;
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long def) {
        Object val = data.get(key);
        if (val instanceof Number n) return n.longValue();
        if (val instanceof String s) {
            try { return Long.parseLong(s); } catch (Exception e) { return def; }
        }
        return def;
    }

    public boolean getBool(String key) {
        return getBool(key, false);
    }

    public boolean getBool(String key, boolean def) {
        Object val = data.get(key);
        if (val instanceof Boolean b) return b;
        if (val instanceof String s) return Boolean.parseBoolean(s);
        return def;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    /** Get all data (read-only snapshot). */
    public Map<String, Object> all() {
        return Map.copyOf(data);
    }
}
