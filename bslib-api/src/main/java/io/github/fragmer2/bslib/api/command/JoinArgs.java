package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;

/**
 * Joins all remaining arguments into a single string.
 * Must be the last parameter.
 *
 * Usage:
 *   @Command("msg")
 *   public void msg(Player sender, Player target, @JoinArgs String message) {}
 *   // /msg Steve Hello how are you? → message = "Hello how are you?"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface JoinArgs {}
