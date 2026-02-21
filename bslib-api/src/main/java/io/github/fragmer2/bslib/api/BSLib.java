package io.github.fragmer2.bslib.api;

import io.github.fragmer2.bslib.api.command.CommandRegistry;
import io.github.fragmer2.bslib.api.command.CommandRegistryFactory;
import io.github.fragmer2.bslib.api.di.ServiceContainer;
import io.github.fragmer2.bslib.api.module.ModuleManager;
import org.bukkit.plugin.Plugin;

public final class BSLib {
    private static CommandRegistryFactory commandFactory;
    private static ServiceContainer container;
    private static ModuleManager moduleManager;

    private BSLib() {}

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
}
