package io.github.fragmer2.bslib.api.module;

import io.github.fragmer2.bslib.api.di.ServiceContainer;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages BSLib modules lifecycle.
 *
 * Usage:
 *   ModuleManager mm = BSLib.getModuleManager();
 *   mm.register(new GuiModule());
 *   mm.register(new ScoreboardModule());
 *   mm.enableAll();
 */
public class ModuleManager {
    private final Plugin plugin;
    private final ServiceContainer container;
    private final Logger logger;
    private final Map<String, Module> modules = new LinkedHashMap<>();
    private final Set<String> enabled = new HashSet<>();

    public ModuleManager(Plugin plugin, ServiceContainer container) {
        this.plugin = plugin;
        this.container = container;
        this.logger = plugin.getLogger();
    }

    /**
     * Register a module.
     */
    public void register(Module module) {
        if (modules.containsKey(module.getId())) {
            logger.warning("Module already registered: " + module.getId());
            return;
        }
        modules.put(module.getId(), module);
    }

    /**
     * Enable a specific module (resolves dependencies first).
     */
    public boolean enable(String moduleId) {
        if (enabled.contains(moduleId)) return true;

        Module module = modules.get(moduleId);
        if (module == null) {
            logger.warning("Module not found: " + moduleId);
            return false;
        }

        // Resolve dependencies
        for (String dep : module.getDependencies()) {
            if (!enabled.contains(dep)) {
                if (!enable(dep)) {
                    logger.severe("Cannot enable " + moduleId + ": missing dependency " + dep);
                    return false;
                }
            }
        }

        try {
            module.onEnable(plugin, container);
            enabled.add(moduleId);
            logger.info("Module enabled: " + module.getName() + " v" + module.getVersion());
            return true;
        } catch (Exception e) {
            logger.severe("Failed to enable module: " + moduleId);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Enable all registered modules in dependency order.
     */
    public void enableAll() {
        // Topological sort by dependencies
        List<String> sorted = topologicalSort();
        for (String id : sorted) {
            enable(id);
        }
    }

    /**
     * Disable a specific module.
     */
    public void disable(String moduleId) {
        Module module = modules.get(moduleId);
        if (module != null && enabled.contains(moduleId)) {
            try {
                module.onDisable();
            } catch (Exception e) {
                logger.warning("Error disabling module: " + moduleId);
                e.printStackTrace();
            }
            enabled.remove(moduleId);
            logger.info("Module disabled: " + module.getName());
        }
    }

    /**
     * Disable all modules (reverse order).
     */
    public void disableAll() {
        List<String> sorted = new ArrayList<>(enabled);
        Collections.reverse(sorted);
        for (String id : sorted) {
            disable(id);
        }
    }

    public boolean isEnabled(String moduleId) {
        return enabled.contains(moduleId);
    }

    public Module getModule(String moduleId) {
        return modules.get(moduleId);
    }

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    // Simple topological sort
    private List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (String id : modules.keySet()) {
            if (!visited.contains(id)) {
                visit(id, visited, visiting, result);
            }
        }
        return result;
    }

    private void visit(String id, Set<String> visited, Set<String> visiting, List<String> result) {
        if (visited.contains(id)) return;
        if (visiting.contains(id)) {
            logger.warning("Circular dependency detected involving: " + id);
            return;
        }
        visiting.add(id);
        Module module = modules.get(id);
        if (module != null) {
            for (String dep : module.getDependencies()) {
                visit(dep, visited, visiting, result);
            }
        }
        visiting.remove(id);
        visited.add(id);
        result.add(id);
    }
}
