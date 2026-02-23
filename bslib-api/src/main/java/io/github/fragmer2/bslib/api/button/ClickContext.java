package io.github.fragmer2.bslib.api.button;

import io.github.fragmer2.bslib.api.menu.MenuView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickContext {
    private final Player player;
    private final InventoryClickEvent event;
    private final MenuView view;

    public ClickContext(Player player, InventoryClickEvent event, MenuView view) {
        this.player = player;
        this.event = event;
        this.view = view;
    }

    // ========== Base accessors ==========

    public Player player() { return player; }

    /** Raw Bukkit event — for advanced use. */
    public InventoryClickEvent event() { return event; }

    /** Alias for event() — matches documentation naming. */
    public InventoryClickEvent getClickEvent() { return event; }

    public MenuView view() { return view; }

    // ========== Click type helpers ==========

    /**
     * Returns true if the player was holding Shift while clicking.
     */
    public boolean isShiftClick() {
        return event.isShiftClick();
    }

    /**
     * Returns true if this was a left mouse button click (with or without Shift).
     */
    public boolean isLeftClick() {
        return event.isLeftClick();
    }

    /**
     * Returns true if this was a right mouse button click (with or without Shift).
     */
    public boolean isRightClick() {
        return event.isRightClick();
    }

    /**
     * Returns true if this was a Shift + Left click.
     */
    public boolean isShiftLeftClick() {
        return event.getClick() == ClickType.SHIFT_LEFT;
    }

    /**
     * Returns true if this was a Shift + Right click.
     */
    public boolean isShiftRightClick() {
        return event.getClick() == ClickType.SHIFT_RIGHT;
    }

    /**
     * Returns true if this was a middle mouse button click.
     */
    public boolean isMiddleClick() {
        return event.getClick() == ClickType.MIDDLE;
    }

    /**
     * Returns true if this was a number key press (hotbar swap).
     */
    public boolean isNumberKey() {
        return event.getClick() == ClickType.NUMBER_KEY;
    }

    /**
     * Returns true if this was a double click.
     */
    public boolean isDoubleClick() {
        return event.getClick() == ClickType.DOUBLE_CLICK;
    }

    /**
     * Returns the raw Bukkit ClickType for full control.
     */
    public ClickType getClickType() {
        return event.getClick();
    }

    /**
     * Returns the slot number that was clicked.
     */
    public int getSlot() {
        return event.getSlot();
    }

    // ========== Menu actions ==========

    /** Close the menu. */
    public void close() { view.close(); }

    /** Re-render the menu (useful after data changes). */
    public void update() { view.update(); }

    /** Navigate back to the previous menu. */
    public void back() {
        io.github.fragmer2.bslib.api.GuiManagerProvider.get().back(player);
    }
}

