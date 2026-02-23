package io.github.fragmer2.bslib.api.module;

import org.bukkit.event.Listener;

/**
 * Abstract base class for a BSLib module, providing convenience methods.
 * Simplifies listener and command registration.
 */
public abstract class AbstractModule implements BSModule {

    protected ModuleContext context;

    @Override
    public void onEnable(ModuleContext context) {
        this.context = context;
    }

    /**
     * Register Bukkit event listeners.
     * The objects will be automatically injected with services.
     */
    protected void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            context.container().inject(listener);
            context.plugin().getServer().getPluginManager().registerEvents(listener, context.plugin());
        }
    }

    /**
     * Register command handlers.
     * The objects will be automatically injected with services.
     */
    protected void registerCommands(Object... handlers) {
        // Assuming a CommandRegistry is available via the ServiceContainer
        // This part needs to be adapted to your actual CommandRegistry implementation
    }
}
