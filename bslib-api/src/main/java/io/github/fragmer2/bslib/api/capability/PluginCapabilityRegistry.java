package io.github.fragmer2.bslib.api.capability;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Runtime capability registry for plugin ecosystem dependency contracts.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Capability contract may expand")
public final class PluginCapabilityRegistry {
    private static final Map<String, Set<String>> provided = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> required = new ConcurrentHashMap<>();

    private PluginCapabilityRegistry() {}

    public static void provide(String pluginName, String... capabilities) {
        provided.computeIfAbsent(pluginName, k -> ConcurrentHashMap.newKeySet()).addAll(List.of(capabilities));
    }

    public static void require(String pluginName, String... capabilities) {
        required.computeIfAbsent(pluginName, k -> ConcurrentHashMap.newKeySet()).addAll(List.of(capabilities));
    }

    public static List<String> validateMissing(String pluginName) {
        Set<String> req = required.getOrDefault(pluginName, Set.of());
        Set<String> allProvided = new HashSet<>();
        provided.values().forEach(allProvided::addAll);
        List<String> missing = new ArrayList<>();
        for (String capability : req) {
            if (!allProvided.contains(capability)) {
                missing.add(capability);
            }
        }
        return missing;
    }

    public static Set<String> providedBy(String pluginName) {
        return Collections.unmodifiableSet(provided.getOrDefault(pluginName, Set.of()));
    }

    public static void clear() {
        provided.clear();
        required.clear();
    }
}
