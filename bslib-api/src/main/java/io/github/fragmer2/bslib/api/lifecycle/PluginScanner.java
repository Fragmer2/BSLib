package io.github.fragmer2.bslib.api.lifecycle;

import io.github.fragmer2.bslib.api.command.Command;
import io.github.fragmer2.bslib.api.di.Service;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * Classpath scanner — finds @Command, Listener, and @Service classes in a plugin JAR.
 */
public final class PluginScanner {

    private PluginScanner() {}

    public static ScanResult scan(JavaPlugin plugin, AutoScan config) {
        Logger log = plugin.getLogger();
        ScanResult result = new ScanResult();
        ClassLoader cl = plugin.getClass().getClassLoader();

        Set<String> basePackages = config.packages().length > 0
                ? Set.of(config.packages())
                : Set.of(); // empty = scan all

        Set<String> excludePackages = Set.of(config.excludePackages());

        // Find the plugin's JAR file
        File jarFile = getPluginJar(plugin);
        if (jarFile == null) {
            log.warning("[AutoScan] Cannot locate plugin JAR — skipping scan");
            return result;
        }

        // Scan JAR entries
        List<String> classNames = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.endsWith(".class") || name.contains("$")) continue; // skip inner classes

                String className = name.replace('/', '.').replace(".class", "");

                // Package filter
                if (!basePackages.isEmpty()) {
                    boolean matches = basePackages.stream().anyMatch(className::startsWith);
                    if (!matches) continue;
                }
                if (excludePackages.stream().anyMatch(className::startsWith)) continue;

                classNames.add(className);
            }
        } catch (IOException e) {
            log.warning("[AutoScan] Failed to read JAR: " + e.getMessage());
            return result;
        }

        // Load and classify
        for (String className : classNames) {
            try {
                Class<?> clazz = cl.loadClass(className);

                // Skip abstract, interfaces, the plugin class itself
                if (Modifier.isAbstract(clazz.getModifiers())) continue;
                if (clazz.isInterface()) continue;
                if (clazz == plugin.getClass()) continue;

                // @Command class or has @Command methods
                if (clazz.isAnnotationPresent(Command.class) || hasCommandMethods(clazz)) {
                    result.commandClasses.add(clazz);
                }

                // Listener implementation — either implements Listener or has @EventListener annotation
                boolean isListener = Listener.class.isAssignableFrom(clazz)
                        || clazz.isAnnotationPresent(EventListener.class)
                        || hasEventHandlerMethods(clazz);
                if (isListener && !result.commandClasses.contains(clazz)) {
                    result.listenerClasses.add(clazz);
                }

                // @Service
                if (clazz.isAnnotationPresent(Service.class)) {
                    result.serviceClasses.add(clazz);
                }

                // @State (persistence)
                if (clazz.isAnnotationPresent(io.github.fragmer2.bslib.api.state.State.class)) {
                    result.stateClasses.add(clazz);
                }

            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // Class has missing dependencies — skip silently
            }
        }

        log.info("[AutoScan] Found: " + result.commandClasses.size() + " commands, " +
                result.listenerClasses.size() + " listeners, " +
                result.serviceClasses.size() + " services, " +
                result.stateClasses.size() + " states");
        return result;
    }

    private static boolean hasCommandMethods(Class<?> clazz) {
        try {
            return Arrays.stream(clazz.getDeclaredMethods())
                    .anyMatch(m -> m.isAnnotationPresent(Command.class) ||
                            m.isAnnotationPresent(io.github.fragmer2.bslib.api.command.Subcommand.class));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if a class has any @EventHandler methods.
     * Allows classes without 'implements Listener' to be auto-detected.
     */
    private static boolean hasEventHandlerMethods(Class<?> clazz) {
        try {
            return Arrays.stream(clazz.getDeclaredMethods())
                    .anyMatch(m -> m.isAnnotationPresent(EventHandler.class));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Try to locate the plugin's JAR file.
     */
    private static File getPluginJar(JavaPlugin plugin) {
        try {
            // JavaPlugin stores its file — accessible via reflection
            java.lang.reflect.Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
            getFile.setAccessible(true);
            return (File) getFile.invoke(plugin);
        } catch (Exception e) {
            // Fallback: check data folder parent
            File pluginsDir = plugin.getDataFolder().getParentFile();
            if (pluginsDir != null) {
                for (File f : Objects.requireNonNull(pluginsDir.listFiles())) {
                    if (f.getName().toLowerCase().contains(plugin.getName().toLowerCase())
                            && f.getName().endsWith(".jar")) {
                        return f;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Instantiate a class via its no-arg constructor.
     * Returns null if not possible.
     */
    public static Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    // ========== Result ==========

    public static class ScanResult {
        public final List<Class<?>> commandClasses = new ArrayList<>();
        public final List<Class<?>> listenerClasses = new ArrayList<>();
        public final List<Class<?>> serviceClasses = new ArrayList<>();
        public final List<Class<?>> stateClasses = new ArrayList<>();
    }
}
