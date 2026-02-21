package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;

/**
 * Specifies custom tab-completion for a parameter.
 *
 * Usage:
 *   @Command("tp")
 *   public void tp(Player sender, @TabComplete(TabType.ONLINE_PLAYERS) Player target) {}
 *
 *   @Command("give")
 *   public void give(Player p, @TabComplete(TabType.MATERIALS) Material mat, int amount) {}
 *
 *   @Command("warp")
 *   public void warp(Player p, @TabComplete(suggest = {"spawn", "shop", "arena"}) String warp) {}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TabComplete {
    TabType value() default TabType.NONE;
    String[] suggest() default {};
}
