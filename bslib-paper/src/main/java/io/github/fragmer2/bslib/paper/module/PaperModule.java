package io.github.fragmer2.bslib.paper.module;

import io.github.fragmer2.bslib.paper.BSLibPlugin;

/**
 * Base interface for a BSLib Paper module.
 * Modules are components of the core BSLib plugin that can be enabled or disabled.
 */
public interface PaperModule {

    /**
     * A unique identifier for this module (e.g., "bslib-gui").
     * @return The module ID.
     */
    String getId();

    /**
     * Called when the module is enabled.
     * @param plugin The main BSLibPlugin instance.
     * @param context The context providing access to shared services.
     */
    void onEnable(BSLibPlugin plugin, PaperModuleContext context);

    /**
     * Called when the module is disabled.
     */
    void onDisable();
}
