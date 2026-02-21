package io.github.fragmer2.bslib.api.menu;

/**
 * Inventory interaction policies for menu security.
 *
 * Usage:
 *   menu.secure();                                // block everything
 *   menu.policy(InventoryPolicy.NO_SHIFT);        // block shift-click
 *   menu.policy(InventoryPolicy.NO_DRAG);         // block drag
 *   menu.policy(InventoryPolicy.NO_SHIFT, InventoryPolicy.NO_NUMBER_KEY);
 */
public enum InventoryPolicy {
    /** Block shift-click (prevents items going to/from player inv). */
    NO_SHIFT,
    /** Block drag events. */
    NO_DRAG,
    /** Block number key hotbar swap. */
    NO_NUMBER_KEY,
    /** Block double-click collection. */
    NO_COLLECT,
    /** Block all interaction with player's own inventory while menu is open. */
    NO_BOTTOM_INV,
    /** Block placing items into the menu. */
    NO_PLACE,
    /** Block creative middle-click clone. */
    NO_CLONE
}
