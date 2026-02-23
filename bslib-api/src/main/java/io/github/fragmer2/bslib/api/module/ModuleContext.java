package io.github.fragmer2.bslib.api.module;

import io.github.fragmer2.bslib.api.di.ServiceContainer;
import org.bukkit.plugin.Plugin;
import java.util.logging.Logger;

/**
 * Provides context to a module during its lifecycle.
 * This includes access to the core plugin, logger, and DI container.
 */
public final class ModuleContext {
    private final Plugin plugin;
    private final ServiceContainer container;
    private final Logger logger;

    public ModuleContext(Plugin plugin, ServiceContainer container) {
        this.plugin = plugin;
        this.container = container;
        this.logger = plugin.getLogger();
    }

    /** The Bukkit Plugin instance. */
    public Plugin plugin() {
        return plugin;
    }

    /** The BSLib dependency injection container. */
    public ServiceContainer container() {
        return container;
    }

    /** The plugin's logger instance. */
    public Logger logger() {
        return logger;
    }
}
