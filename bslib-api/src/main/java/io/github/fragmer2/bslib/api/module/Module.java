package io.github.fragmer2.bslib.api.module;

import io.github.fragmer2.bslib.api.di.ServiceContainer;
import org.bukkit.plugin.Plugin;

/**
 * Base interface for BSLib modules.
 *
 * Modules are independent features that can be enabled/disabled:
 *   brae-gui, brae-command, brae-config, brae-npc, brae-scoreboard
 *
 * Usage:
 *   public class ScoreboardModule implements Module {
 *       @Override public String getId() { return "brae-scoreboard"; }
 *       @Override public void onEnable(Plugin plugin, ServiceContainer container) {
 *           container.register(ScoreboardManager.class, new ScoreboardManagerImpl());
 *       }
 *   }
 *
 * Registration:
 *   BSLib.getModuleManager().register(new ScoreboardModule());
 */
public interface Module {

    /**
     * Unique module ID (e.g., "brae-gui", "brae-scoreboard").
     */
    String getId();

    /**
     * Display name for logging.
     */
    default String getName() {
        return getId();
    }

    /**
     * Module version.
     */
    default String getVersion() {
        return "1.0.0";
    }

    /**
     * Called when the module is enabled.
     * Register services, listeners, commands here.
     */
    void onEnable(Plugin plugin, ServiceContainer container);

    /**
     * Called when the module is disabled.
     * Clean up resources here.
     */
    default void onDisable() {}

    /**
     * Module dependencies (other module IDs that must load first).
     */
    default String[] getDependencies() {
        return new String[0];
    }
}
