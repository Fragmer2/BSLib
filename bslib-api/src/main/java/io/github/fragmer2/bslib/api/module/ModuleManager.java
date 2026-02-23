package io.github.fragmer2.bslib.api.module;

import io.github.fragmer2.bslib.api.di.ServiceContainer;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages the lifecycle of all BSLib modules.
 * Modules are registered via {@link ModuleRegistry} and enabled/disabled automatically.
 */
public class ModuleManager {
    private final Plugin plugin;
    private final ServiceContainer container;
    private final Logger logger;
    private final Map<String, BSModule> modules = new LinkedHashMap<>();
    private final Set<String> enabled = new HashSet<>();
    private final List<String> enableOrder = new ArrayList<>();

    public ModuleManager(Plugin plugin, ServiceContainer container) {
        this.plugin = plugin;
        this.container = container;
        this.logger = plugin.getLogger();
    }

    /**
     * Register a module instance.
     */
    public void register(BSModule module) {
        if (modules.containsKey(module.getId())) {
            logger.warning("[BSLib] Module already registered: " + module.getId());
            return;
        }
        modules.put(module.getId(), module);
    }

    /**
     * Register all modules from the central {@link ModuleRegistry}.
     * Call this once on startup.
     */
    public void registerFromRegistry() {
        for (var supplier : ModuleRegistry.getModuleSuppliers()) {
            register(supplier.get());
        }
    }

    /**
     * Enable a specific module (resolves dependencies first).
     */
    public boolean enable(String moduleId) {
        return enable(moduleId, new LinkedHashSet<>());
    }

    private boolean enable(String moduleId, Set<String> enabling) {
        if (enabled.contains(moduleId)) return true;
        if (!enabling.add(moduleId)) {
            logger.severe("[BSLib] Circular dependency detected while enabling: " + String.join(" -> ", enabling) + " -> " + moduleId);
            return false;
        }

        BSModule module = modules.get(moduleId);
        if (module == null) {
            logger.warning("[BSLib] Module not found: " + moduleId);
            enabling.remove(moduleId);
            return false;
        }

        // Resolve dependencies first
        for (String dep : module.getDependencies()) {
            if (!enabled.contains(dep)) {
                if (!modules.containsKey(dep)) {
                    logger.severe("[BSLib] Cannot enable " + moduleId + ": dependency module is not registered: '" + dep + "'");
                    enabling.remove(moduleId);
                    return false;
                }
                if (!enable(dep, enabling)) {
                    logger.severe("[BSLib] Cannot enable " + moduleId + ": missing dependency '" + dep + "'");
                    enabling.remove(moduleId);
                    return false;
                }
            }
        }

        try {
            ModuleContext ctx = new ModuleContext(plugin, container);
            module.onEnable(ctx);
            enabled.add(moduleId);
            enableOrder.remove(moduleId);
            enableOrder.add(moduleId);
            logger.info("[BSLib] Module enabled: " + module.getName() + " v" + module.getVersion());
            enabling.remove(moduleId);
            return true;
        } catch (Exception e) {
            logger.severe("[BSLib] Failed to enable module: " + moduleId);
            e.printStackTrace();
            enabling.remove(moduleId);
            return false;
        }
    }

    /**
     * Enable all registered modules in dependency order.
     */
    public void enableAll() {
        List<String> sorted = topologicalSort();
        for (String id : sorted) {
            enable(id);
        }
    }

    /**
     * Disable a specific module.
     */
    public void disable(String moduleId) {
        BSModule module = modules.get(moduleId);
        if (module != null && enabled.contains(moduleId)) {
            try {
                module.onDisable();
            } catch (Exception e) {
                logger.warning("[BSLib] Error disabling module: " + moduleId);
                e.printStackTrace();
            }
            enabled.remove(moduleId);
            enableOrder.remove(moduleId);
            logger.info("[BSLib] Module disabled: " + module.getName());
        }
    }

    /**
     * Disable all modules in reverse order.
     */
    public void disableAll() {
        List<String> sorted = new ArrayList<>(enableOrder);
        Collections.reverse(sorted);
        for (String id : sorted) {
            disable(id);
        }
    }

    public boolean isEnabled(String moduleId) {
        return enabled.contains(moduleId);
    }

    public BSModule getModule(String moduleId) {
        return modules.get(moduleId);
    }

    public Collection<BSModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    // Simple topological sort for dependency resolution
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
            logger.warning("[BSLib] Circular dependency detected involving: " + id);
            return;
        }
        visiting.add(id);
        BSModule module = modules.get(id);
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
