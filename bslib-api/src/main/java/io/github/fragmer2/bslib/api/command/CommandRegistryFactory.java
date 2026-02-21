package io.github.fragmer2.bslib.api.command;

import org.bukkit.plugin.Plugin;

public interface CommandRegistryFactory {
    CommandRegistry create(Plugin plugin);
}