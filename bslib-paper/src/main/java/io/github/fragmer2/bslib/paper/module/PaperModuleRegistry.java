package io.github.fragmer2.bslib.paper.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry for all BSLib Paper modules.
 * To add a new module, simply add its constructor reference to the `modules` list.
 */
public final class PaperModuleRegistry {

    static final List<Supplier<PaperModule>> modules = new ArrayList<>();

    static {
        // === REGISTER YOUR PAPER MODULES HERE ===
        // Example:
        // modules.add(GuiPaperModule::new);
        // modules.add(CommandsPaperModule::new);
    }

    private PaperModuleRegistry() {}
}
