package io.github.fragmer2.bslib.api.state;

import java.lang.annotation.*;

/**
 * Marks the key field in a @State class.
 * The key uniquely identifies the state instance (typically UUID).
 *
 *   @State("player-data")
 *   public class PlayerData {
 *       @StateKey UUID uuid;
 *       Reactive<Integer> coins = Reactive.of(0);
 *   }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StateKey {}
