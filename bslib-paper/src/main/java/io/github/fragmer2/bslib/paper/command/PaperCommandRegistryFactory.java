package io.github.fragmer2.bslib.paper.command;

import io.github.fragmer2.bslib.api.command.CommandRegistry;
import io.github.fragmer2.bslib.api.command.CommandRegistryFactory;
import org.bukkit.plugin.Plugin;

public class PaperCommandRegistryFactory implements CommandRegistryFactory {
    @Override
    public CommandRegistry create(Plugin plugin) {
        return new PaperCommandRegistry(plugin);
    }
}