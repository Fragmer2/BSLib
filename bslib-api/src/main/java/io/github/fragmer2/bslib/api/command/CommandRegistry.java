package io.github.fragmer2.bslib.api.command;

import java.util.List;
import java.util.function.Function;

public interface CommandRegistry {
    /**
     * Register all @Command/@Subcommand methods from an instance.
     */
    void register(Object commandsInstance);

    /**
     * Register a custom argument converter for a type.
     * Usage:
     *   registry.registerConverter(GameMode.class, s -> GameMode.valueOf(s.toUpperCase()));
     */
    <T> void registerConverter(Class<T> type, Function<String, T> converter);

    /**
     * Register a custom converter with tab-completion suggestions.
     */
    <T> void registerConverter(Class<T> type, Function<String, T> converter, Function<String, List<String>> tabCompleter);
}
