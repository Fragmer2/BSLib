package io.github.fragmer2.bslib.paper.module;

import io.github.fragmer2.bslib.paper.BSLibPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Manages the lifecycle of all BSLib Paper modules.
 * Modules are enabled in registration order and disabled in reverse order.
 */
public class PaperModuleManager {

    private final BSLibPlugin plugin;
    private final PaperModuleContext context;
    private final Logger logger;
    private final List<PaperModule> enabledModules = new ArrayList<>();

    public PaperModuleManager(BSLibPlugin plugin, PaperModuleContext context) {
        this.plugin = plugin;
        this.context = context;
        this.logger = plugin.getLogger();
    }

    /**
     * Enable all modules registered in PaperModuleRegistry.
     */
    public void enableAll() {
        logger.info("Enabling BSLib Paper modules...");
        for (var supplier : PaperModuleRegistry.modules) {
            PaperModule module = null;
            try {
                module = supplier.get();
                logger.info("  [+] " + module.getId());
                module.onEnable(plugin, context);
                enabledModules.add(module);
            } catch (Exception e) {
                String id = module != null ? module.getId() : "unknown";
                logger.severe("Failed to enable Paper module [" + id + "]: " + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.info(enabledModules.size() + " Paper module(s) enabled.");
    }

    /**
     * Disable all enabled modules in reverse order.
     */
    public void disableAll() {
        logger.info("Disabling " + enabledModules.size() + " Paper module(s)...");
        for (int i = enabledModules.size() - 1; i >= 0; i--) {
            PaperModule module = enabledModules.get(i);
            try {
                module.onDisable();
            } catch (Exception e) {
                logger.severe("Failed to disable module [" + module.getId() + "]: " + e.getMessage());
                e.printStackTrace();
            }
        }
        enabledModules.clear();
    }

    /**
     * Returns the number of currently enabled modules.
     */
    public int getEnabledCount() {
        return enabledModules.size();
    }

    /**
     * Returns a list of formatted status lines for /bslib modules.
     */
    public List<String> listModules() {
        if (enabledModules.isEmpty()) {
            return List.of("§7No Paper modules registered.");
        }
        List<String> lines = new ArrayList<>();
        for (PaperModule m : enabledModules) {
            lines.add("§a● §f" + m.getId());
        }
        return lines;
    }
}
