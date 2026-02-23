package io.github.fragmer2.bslib.api.menu;

import io.github.fragmer2.bslib.api.button.Button;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A menu that uses a string layout pattern to position buttons.
 *
 * Usage:
 *   public class ShopMenu extends LayoutMenu {
 *       public ShopMenu() {
 *           super("Shop", 3,
 *               "#########",
 *               "#.A.B.C.#",
 *               "#########"
 *           );
 *       }
 *
 *       @Override
 *       protected void setupLayout() {
 *           bind('#', Button.of(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
 *           bind('A', Button.of(new ItemStack(Material.DIAMOND)).click(c -> buy(c.player())));
 *           bind('.', null); // empty slot — no button
 *       }
 *   }
 */
public abstract class LayoutMenu extends Menu {

    private final Map<Character, Button> buttonMap = new HashMap<>();
    private final String[] layout;

    /**
     * Create a layout menu.
     *
     * @param title  Inventory title (supports MiniMessage format)
     * @param rows   Number of rows (must match layout line count)
     * @param layout String patterns, one per row, each 9 characters wide
     */
    public LayoutMenu(String title, int rows, String... layout) {
        super(title, rows);
        if (layout.length != rows) {
            throw new IllegalArgumentException(
                    "LayoutMenu '" + title + "': layout has " + layout.length +
                    " lines but rows=" + rows + ". They must match.");
        }
        this.layout = layout;
        setupLayout();   // user fills buttonMap via bind()
        applyLayout();   // place buttons according to the pattern
    }

    @Override
    protected final void setup() {
        // Initialization is done in the constructor via setupLayout() + applyLayout()
    }

    /**
     * Override this method to bind characters to buttons.
     * Call bind(char, Button) for each symbol used in the layout.
     */
    protected abstract void setupLayout();

    /**
     * Bind a character symbol to a button.
     * Symbols used in the layout string that are not bound will be left empty.
     *
     * @param symbol  The character used in the layout pattern
     * @param button  The button to place at that position (null = leave empty)
     */
    protected void bind(char symbol, Button button) {
        if (button != null) {
            buttonMap.put(symbol, button);
        }
    }

    /**
     * Update a button for a specific symbol and re-apply the layout.
     * Useful for dynamic menus where button content changes.
     *
     * @param symbol  The character symbol to update
     * @param button  The new button
     */
    protected void updateBind(char symbol, Button button) {
        bind(symbol, button);
        applyLayout();
    }

    /**
     * Fill all slots that match the given symbol with the given material.
     * Shorthand for bind(symbol, Button.of(new ItemStack(material))).
     */
    protected void fill(char symbol, Material material) {
        bind(symbol, Button.of(new ItemStack(material)));
    }

    /**
     * Fill all slots that match the given symbol with the given item.
     */
    protected void fill(char symbol, ItemStack item) {
        bind(symbol, Button.of(item));
    }

    /**
     * Get the slot index for the first occurrence of a symbol in the layout.
     * Returns -1 if the symbol is not found.
     */
    protected int slotOf(char symbol) {
        for (int row = 0; row < layout.length; row++) {
            int col = layout[row].indexOf(symbol);
            if (col >= 0) return row * 9 + col;
        }
        return -1;
    }

    /**
     * Get all slot indices for a given symbol.
     */
    protected int[] slotsOf(char symbol) {
        java.util.List<Integer> slots = new java.util.ArrayList<>();
        for (int row = 0; row < layout.length; row++) {
            String line = layout[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == symbol) {
                    slots.add(row * 9 + col);
                }
            }
        }
        return slots.stream().mapToInt(Integer::intValue).toArray();
    }

    private void applyLayout() {
        for (int row = 0; row < layout.length; row++) {
            String line = layout[row];
            for (int col = 0; col < Math.min(line.length(), 9); col++) {
                char symbol = line.charAt(col);
                if (symbol == ' ' || symbol == '.') continue; // skip empty slots
                Button btn = buttonMap.get(symbol);
                if (btn != null) {
                    int slot = row * 9 + col;
                    setButton(slot, btn);
                }
            }
        }
    }
}