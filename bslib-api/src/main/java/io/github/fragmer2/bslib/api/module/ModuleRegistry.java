package io.github.fragmer2.bslib.api.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A central registry for all available BSLib modules.
 * This is the single source of truth for what modules exist in the system.
 *
 * To add a new module to BSLib, simply add it to the list in the static initializer block.
 * The ModuleManager will automatically pick it up.
 *
 * Example:
 *   private static final List<Supplier<BSModule>> modules = new ArrayList<>();
 *   static {
 *       modules.add(CommandsModule::new);
 *       modules.add(GuiModule::new);
 *       // To add a new module, just add a line here:
 *       // modules.add(MyNewAwesomeModule::new);
 *   }
 */
public final class ModuleRegistry {

    private static final List<Supplier<BSModule>> modules = new ArrayList<>();

    // --- Register all core BSLib modules here ---
    static {
        // Example modules (replace with your actual modules)
        // modules.add(CommandsModule::new);
        // modules.add(GuiModule::new);
        // modules.add(StatesModule::new);
    }

    private ModuleRegistry() {}

    /**
     * Get a list of suppliers for all registered modules.
     * Each supplier creates a new instance of a module.
     */
    public static List<Supplier<BSModule>> getModuleSuppliers() {
        return modules;
    }
}
