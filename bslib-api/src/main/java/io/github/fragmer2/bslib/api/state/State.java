package io.github.fragmer2.bslib.api.state;

import java.lang.annotation.*;

/**
 * Marks a class as persistent state.
 *
 * Usage:
 *   @State("player-data")
 *   public class PlayerData {
 *       UUID uuid;
 *       Reactive<Integer> coins = Reactive.of(0);
 *       Reactive<Integer> level = Reactive.of(1);
 *       Reactive<String> rank = Reactive.of("default");
 *       List<String> homes = new ArrayList<>();
 *   }
 *
 * Features:
 * - Auto-serializes Reactive fields, primitives, collections, maps
 * - Async save/load with sync callbacks
 * - Dirty tracking — only saves when data actually changed
 * - Autosave on interval
 * - Auto-save on quit, auto-load on join
 *
 * Backend options: YAML (default), JSON
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface State {
    /** Storage identifier (folder name). */
    String value();

    /** Auto-save interval in seconds. 0 = no auto-save. Default: 300 (5 min). */
    int autosaveSeconds() default 300;

    /** Storage backend. */
    Backend backend() default Backend.YAML;

    enum Backend { YAML, JSON }
}
