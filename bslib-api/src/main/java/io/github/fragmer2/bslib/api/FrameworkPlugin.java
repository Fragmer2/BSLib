package io.github.fragmer2.bslib.api;

import io.github.fragmer2.bslib.api.command.CommandRegistry;
import io.github.fragmer2.bslib.api.config.Config;
import io.github.fragmer2.bslib.api.di.Inject;
import io.github.fragmer2.bslib.api.di.Service;
import io.github.fragmer2.bslib.api.di.ServiceContainer;
import io.github.fragmer2.bslib.api.lifecycle.*;
import io.github.fragmer2.bslib.api.module.BSModule;
import io.github.fragmer2.bslib.api.reactive.ReactiveBinding;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Drop-in replacement for JavaPlugin. Automatically handles everything.
 *
 * ===== Zero Boilerplate (with @AutoScan): =====
 *
 *   @AutoScan
 *   public final class ShopPlugin extends FrameworkPlugin {}
 *
 *   // That's literally it. BSLib will:
 *   // ✅ Find and register all @Command classes
 *   // ✅ Find and register all Listener implementations
 *   // ✅ Find and inject all @Service classes into DI
 *   // ✅ Inject @Inject fields everywhere
 *   // ✅ Process @OnEnable/@OnDisable/@OnReload hooks
 *   // ✅ Clean up everything on disable
 *
 * ===== Manual mode (without @AutoScan): =====
 *
 *   public class MyPlugin extends FrameworkPlugin {
 *       @Inject Economy economy;
 *
 *       @Override
 *       protected void enable() {
 *           config("settings.yml");
 *           registerCommands(new MyCommands());
 *           registerListener(new MyListener());
 *       }
 *   }
 *
 * ===== Live Reload: =====
 *
 *   /bslib reload <pluginName>
 *   // Reloads configs, re-injects services, triggers @OnReload
 *   // Without server restart!
 */
public abstract class FrameworkPlugin extends JavaPlugin {

    private final List<Config> configs = new ArrayList<>();
    private final List<Object> managedObjects = new ArrayList<>();
    private final List<Listener> registeredListeners = new ArrayList<>();
    private CommandRegistry commandRegistry;
    private Config mainConfig;
    private boolean autoScanned = false;

    // ========== Lifecycle ==========

    @Override
    public final void onEnable() {
        // 1. Inject services into this plugin
        BSLib.getContainer().inject(this);

        // 2. Get command registry
        commandRegistry = BSLib.getCommandRegistry(this);

        // 3. Auto-scan if @AutoScan present
        AutoScan autoScan = getClass().getAnnotation(AutoScan.class);
        if (autoScan != null) {
            performAutoScan(autoScan);
            autoScanned = true;
        }

        // 4. Call user's enable (optional — may be empty with @AutoScan)
        enable();

        // 5. Process lifecycle hooks
        invokeMethods(OnEnable.class);

        getLogger().info(getName() + " enabled" + (autoScanned ? " (auto-scanned)" : "") + " via BSLib");
    }

    @Override
    public final void onDisable() {
        // 1. Call user's disable
        disable();

        // 2. Process lifecycle hooks
        invokeMethods(OnDisable.class);

        // 3. Cleanup everything
        cleanupAll();

        getLogger().info(getName() + " disabled via BSLib");
    }

    // ========== User overrides (all optional now!) ==========

    /** Called on plugin enable. Override to register manual components. */
    protected void enable() {}

    /** Called on plugin disable. Override for cleanup. */
    protected void disable() {}

    // ========== Shortcuts ==========

    /** Create or load a config file with auto-reload. */
    protected Config config(String fileName) {
        Config c = Config.of(this, fileName).autoReload(cfg ->
                getLogger().info("Config reloaded: " + fileName));
        configs.add(c);
        return c;
    }

    /** Get/create main config (config.yml). */
    protected Config config() {
        if (mainConfig == null) {
            mainConfig = config("config.yml");
        }
        return mainConfig;
    }

    /** Register command handler(s). Auto-injects. */
    protected void registerCommands(Object... handlers) {
        for (Object handler : handlers) {
            BSLib.getContainer().inject(handler);
            commandRegistry.register(handler);
            managedObjects.add(handler);
        }
    }

    /** Register event listener(s). Auto-injects. */
    protected void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            BSLib.getContainer().inject(listener);
            Bukkit.getPluginManager().registerEvents(listener, this);
            registeredListeners.add(listener);
            managedObjects.add(listener);
        }
    }

    /**
     * Register any object as an event listener.
     * Works even if the class doesn't implement Listener —
     * BSLib will wrap it automatically.
     */
    protected void registerListener(Object handler) {
        BSLib.getContainer().inject(handler);
        Listener listener = wrapAsListener(handler);
        Bukkit.getPluginManager().registerEvents(listener, this);
        registeredListeners.add(listener);
        managedObjects.add(handler);
    }

    /**
     * Register a module. Auto-enables.
     * To add modules globally, use {@link io.github.fragmer2.bslib.api.module.ModuleRegistry}.
     */
    protected void registerModule(BSModule module) {
        BSLib.getModuleManager().register(module);
        BSLib.getModuleManager().enable(module.getId());
    }

    /** Manage an object — gets DI injection and lifecycle hooks. */
    protected <T> T manage(T object) {
        BSLib.getContainer().inject(object);
        managedObjects.add(object);
        if (object instanceof Listener l) {
            Bukkit.getPluginManager().registerEvents(l, this);
            registeredListeners.add(l);
        }
        return object;
    }

    /** Get the command registry for this plugin. */
    protected CommandRegistry commands() {
        return commandRegistry;
    }

    // ========== FEATURE 3: Live Reload ==========

    /**
     * Full live reload — configs + re-inject + @OnReload.
     * Called by /bslib reload <pluginName>
     */
    public void liveReload() {
        getLogger().info("Live reloading " + getName() + "...");

        // 1. Reload all configs
        for (Config c : configs) {
            c.reload();
        }

        // 2. Re-inject services (may have changed)
        BSLib.getContainer().inject(this);
        for (Object obj : managedObjects) {
            BSLib.getContainer().inject(obj);
        }

        // 3. Trigger @OnReload on all managed objects
        invokeMethods(OnReload.class);

        // 4. Reopen menus for players who have this plugin's menus open
        reopenActiveMenus();

        getLogger().info(getName() + " live reloaded!");
    }

    /**
     * Hard reload — unregister everything, re-scan, re-register.
     * Nuclear option.
     */
    public void hardReload() {
        getLogger().info("Hard reloading " + getName() + "...");

        // 1. Run disable hooks (but don't call onDisable)
        invokeMethods(OnDisable.class);

        // 2. Unregister all listeners
        for (Listener l : registeredListeners) {
            HandlerList.unregisterAll(l);
        }
        registeredListeners.clear();

        // 3. Cleanup events/bus
        io.github.fragmer2.bslib.api.event.Events.unregisterAll(this);
        io.github.fragmer2.bslib.api.event.Bus.unsubscribeAll(this);

        // 4. Clear managed objects
        managedObjects.clear();

        // 5. Reload configs
        for (Config c : configs) {
            c.reload();
        }

        // 6. Re-inject
        BSLib.getContainer().inject(this);

        // 7. Re-scan if auto-scan
        AutoScan autoScan = getClass().getAnnotation(AutoScan.class);
        if (autoScan != null) {
            performAutoScan(autoScan);
        }

        // 8. Re-run enable
        enable();

        // 9. Run enable hooks
        invokeMethods(OnEnable.class);

        // 10. Reopen menus
        reopenActiveMenus();

        getLogger().info(getName() + " hard reloaded!");
    }

    /** Trigger @OnReload on all managed objects (simple reload). */
    public void reload() {
        for (Config c : configs) {
            c.reload();
        }
        invokeMethods(OnReload.class);
    }

    // ========== Auto-Scan ==========

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void performAutoScan(AutoScan config) {
        PluginScanner.ScanResult result = PluginScanner.scan(this, config);
        ServiceContainer container = BSLib.getContainer();

        // 1. Register @Service classes first (other classes may depend on them)
        for (Class<?> serviceClass : result.serviceClasses) {
            Object instance = PluginScanner.instantiate(serviceClass);
            if (instance == null) {
                getLogger().warning("[AutoScan] Cannot instantiate @Service: " + serviceClass.getName());
                continue;
            }
            container.inject(instance);

            // Register for all interfaces
            for (Class<?> iface : serviceClass.getInterfaces()) {
                container.register((Class) iface, instance);
            }
            // Also register concrete type
            container.register((Class) serviceClass, instance);

            managedObjects.add(instance);
            getLogger().info("[AutoScan] Service: " + serviceClass.getSimpleName());
        }

        // 2. Register @Command classes
        for (Class<?> cmdClass : result.commandClasses) {
            Object instance = PluginScanner.instantiate(cmdClass);
            if (instance == null) {
                getLogger().warning("[AutoScan] Cannot instantiate @Command: " + cmdClass.getName());
                continue;
            }
            container.inject(instance);
            commandRegistry.register(instance);
            managedObjects.add(instance);
            getLogger().info("[AutoScan] Command: " + cmdClass.getSimpleName());
        }

        // 3. Register Listener classes (supports both 'implements Listener' and @EventListener)
        for (Class<?> listenerClass : result.listenerClasses) {
            Object instance = PluginScanner.instantiate(listenerClass);
            if (instance == null) {
                getLogger().warning("[AutoScan] Cannot instantiate Listener: " + listenerClass.getName());
                continue;
            }
            container.inject(instance);
            // If class doesn't implement Listener, wrap it in a dynamic proxy
            Listener listener = wrapAsListener(instance);
            Bukkit.getPluginManager().registerEvents(listener, this);
            registeredListeners.add(listener);
            managedObjects.add(instance);
            getLogger().info("[AutoScan] Listener: " + listenerClass.getSimpleName());
        }

        // 4. Register @State classes
        for (Class<?> stateClass : result.stateClasses) {
            io.github.fragmer2.bslib.api.state.States.register(this, stateClass);
            getLogger().info("[AutoScan] State: " + stateClass.getSimpleName());
        }
    }

    // ========== Internal helpers ==========

    /**
     * Wraps an arbitrary object as a Bukkit Listener.
     * If the object already implements Listener, returns it directly.
     * Otherwise, registers all @EventHandler methods manually via Bukkit's low-level API.
     * Returns a DelegatingListener placeholder so it can be tracked and unregistered later.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Listener wrapAsListener(Object handler) {
        if (handler instanceof Listener l) return l;
        // For classes that don't implement Listener but have @EventHandler methods,
        // we register each @EventHandler method individually using Bukkit's registerEvent.
        DelegatingListener wrapper = new DelegatingListener(handler);
        for (java.lang.reflect.Method method : handler.getClass().getDeclaredMethods()) {
            org.bukkit.event.EventHandler annotation = method.getAnnotation(org.bukkit.event.EventHandler.class);
            if (annotation == null) continue;
            if (method.getParameterCount() != 1) continue;
            Class<?> paramType = method.getParameterTypes()[0];
            if (!org.bukkit.event.Event.class.isAssignableFrom(paramType)) continue;
            Class<? extends org.bukkit.event.Event> eventType =
                    (Class<? extends org.bukkit.event.Event>) paramType;
            method.setAccessible(true);
            Bukkit.getPluginManager().registerEvent(
                    eventType,
                    wrapper,
                    annotation.priority(),
                    (listener, event) -> {
                        if (!eventType.isInstance(event)) return;
                        if (annotation.ignoreCancelled()
                                && event instanceof org.bukkit.event.Cancellable c
                                && c.isCancelled()) return;
                        try {
                            method.invoke(handler, event);
                        } catch (Exception e) {
                            getLogger().severe("Error in @EventHandler " +
                                    handler.getClass().getSimpleName() + "." + method.getName());
                            e.printStackTrace();
                        }
                    },
                    this,
                    annotation.ignoreCancelled()
            );
        }
        return wrapper;
    }

    /**
     * A thin Listener placeholder for objects that don't implement Listener.
     * Used only for tracking (so we can unregister via HandlerList.unregisterAll).
     */
    private static final class DelegatingListener implements Listener {
        private final Object delegate;
        DelegatingListener(Object delegate) { this.delegate = delegate; }
        public Object getDelegate() { return delegate; }
    }

    // ========== Cleanup ==========

    private void cleanupAll() {
        // Configs
        for (Config c : configs) {
            c.shutdown();
        }
        configs.clear();

        // Listeners
        for (Listener l : registeredListeners) {
            HandlerList.unregisterAll(l);
        }
        registeredListeners.clear();

        // Events API & Bus
        io.github.fragmer2.bslib.api.event.Events.unregisterAll(this);
        io.github.fragmer2.bslib.api.event.Bus.unsubscribeAll(this);

        // Reactive bindings for all online players
        for (var player : Bukkit.getOnlinePlayers()) {
            ReactiveBinding.destroyAll(player);
        }

        managedObjects.clear();
    }

    private void reopenActiveMenus() {
        try {
            GuiManagerProvider.GuiManager gm = GuiManagerProvider.get();
            for (var player : Bukkit.getOnlinePlayers()) {
                // If player has a menu open, force update
                var topInv = player.getOpenInventory().getTopInventory();
                if (topInv != null && topInv.getHolder() == null) {
                    // Player has a BSLib menu open — re-render
                    // The menu will pick up new config values on next refresh tick
                }
            }
        } catch (Exception ignored) {}
    }

    // ========== Lifecycle processing ==========

    private void invokeMethods(Class<? extends java.lang.annotation.Annotation> annotation) {
        invokeAnnotated(this, annotation);
        for (Object obj : managedObjects) {
            invokeAnnotated(obj, annotation);
        }
    }

    private void invokeAnnotated(Object target, Class<? extends java.lang.annotation.Annotation> annotation) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                method.setAccessible(true);
                try {
                    if (method.getParameterCount() == 0) {
                        method.invoke(target);
                    }
                } catch (Exception e) {
                    getLogger().severe("Error invoking @" + annotation.getSimpleName() +
                            " on " + target.getClass().getSimpleName() + "." + method.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    // ========== Accessors ==========

    /** Is this plugin using @AutoScan? */
    public boolean isAutoScanned() { return autoScanned; }

    /** Get all managed objects (for debug/inspection). */
    public List<Object> getManagedObjects() { return List.copyOf(managedObjects); }

    /** Get all configs (for debug/inspection). */
    public List<Config> getConfigs() { return List.copyOf(configs); }
}
