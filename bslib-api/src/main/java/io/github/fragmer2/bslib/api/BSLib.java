package io.github.fragmer2.bslib.api;

import io.github.fragmer2.bslib.api.command.CommandRegistry;
import io.github.fragmer2.bslib.api.command.CommandRegistryFactory;
import io.github.fragmer2.bslib.api.di.ServiceContainer;
import io.github.fragmer2.bslib.api.module.BSModule;
import io.github.fragmer2.bslib.api.module.ModuleManager;
import io.github.fragmer2.bslib.api.module.ModuleRegistry;
import org.bukkit.plugin.Plugin;

/**
 * The central access point for the BSLib framework.
 *
 * BSLib is initialized automatically by the BSLib plugin on server startup.
 * All modules listed in {@link ModuleRegistry} are registered and enabled automatically.
 *
 * You can also register custom modules at runtime:
 *   BSLib.getModuleManager().register(new MyModule());
 *   BSLib.getModuleManager().enable("my-module");
 */
public final class BSLib {
    private static CommandRegistryFactory commandFactory;
    private static ServiceContainer container;
    private static ModuleManager moduleManager;

    private BSLib() {}

    // ========== Initialization ==========

    /**
     * Initialize BSLib with the given plugin, container, and command factory.
     * Automatically registers and enables all modules from {@link ModuleRegistry}.
     *
     * This is called internally by the BSLib plugin. You do not need to call this manually.
     */
    public static void initialize(Plugin plugin, ServiceContainer serviceContainer, CommandRegistryFactory factory) {
        container = serviceContainer;
        commandFactory = factory;
        moduleManager = new ModuleManager(plugin, serviceContainer);

        // Load all modules from the central registry
        moduleManager.registerFromRegistry();

        // Enable all registered modules in dependency order
        moduleManager.enableAll();

        plugin.getLogger().info("[BSLib] Initialized with " +
                moduleManager.getModules().size() + " module(s) from ModuleRegistry.");
    }

    // ========== Command ==========

    public static void setCommandRegistryFactory(CommandRegistryFactory factory) {
        commandFactory = factory;
    }

    public static CommandRegistry getCommandRegistry(Plugin plugin) {
        if (commandFactory == null) {
            throw new IllegalStateException("CommandRegistryFactory not initialized. Is BSLib loaded?");
        }
        return commandFactory.create(plugin);
    }

    // ========== DI Container ==========

    public static void setContainer(ServiceContainer c) {
        container = c;
    }

    public static ServiceContainer getContainer() {
        if (container == null) {
            throw new IllegalStateException("ServiceContainer not initialized. Is BSLib loaded?");
        }
        return container;
    }

    // ========== Modules ==========

    public static void setModuleManager(ModuleManager mm) {
        moduleManager = mm;
    }

    public static ModuleManager getModuleManager() {
        if (moduleManager == null) {
            throw new IllegalStateException("ModuleManager not initialized. Is BSLib loaded?");
        }
        return moduleManager;
    }

    /**
     * Shortcut to check if a module is enabled.
     *
     * @param moduleId The module ID (e.g., "bslib-gui")
     */
    public static boolean isModuleEnabled(String moduleId) {
        return moduleManager != null && moduleManager.isEnabled(moduleId);
    }

    /**
     * Shortcut to get a module instance by ID.
     *
     * @param moduleId The module ID
     * @return The module instance, or null if not registered
     */
    public static BSModule getModule(String moduleId) {
        return moduleManager != null ? moduleManager.getModule(moduleId) : null;
    }
}
