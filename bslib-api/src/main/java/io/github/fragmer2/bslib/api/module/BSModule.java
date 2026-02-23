package io.github.fragmer2.bslib.api.module;

/**
 * Base interface for all BSLib modules.
 *
 * A module is a self-contained unit of functionality that can be enabled or disabled,
 * such as a command system, a GUI framework, or an economy integration.
 * Modules are registered in a central {@link ModuleRegistry} and are automatically
 * managed by the BSLib core.
 *
 * @see AbstractModule
 * @see ModuleRegistry
 */
public interface BSModule {

    /**
     * Unique module ID (e.g., "bslib-commands", "bslib-gui").
     * Convention is to use all lowercase with hyphens.
     */
    String getId();

    /**
     * Display name for logging. Defaults to the module ID.
     */
    default String getName() {
        return getId();
    }

    /**
     * Module version string.
     */
    default String getVersion() {
        return "1.0.0";
    }

    /**
     * An array of module IDs that this module depends on.
     * The ModuleManager will ensure dependencies are enabled before this module.
     */
    default String[] getDependencies() {
        return new String[0];
    }

    /**
     * Called when the module is enabled by the ModuleManager.
     * This is where you should register services, listeners, commands, etc.
     *
     * @param context The module context, providing access to the plugin, logger, and DI container.
     */
    void onEnable(ModuleContext context);

    /**
     * Called when the module is disabled.
     * Use this to clean up resources, unregister listeners, etc.
     */
    default void onDisable() {}
}
