package io.github.fragmer2.bslib.api.menu;

import io.github.fragmer2.bslib.api.GuiManagerProvider;
import io.github.fragmer2.bslib.api.button.Button;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Menu {
    private final String title;
    private final int rows;
    private final Map<Integer, Button> buttons = new HashMap<>();
    private final Set<InventoryPolicy> policies = EnumSet.noneOf(InventoryPolicy.class);

    public Menu(String title, int rows) {
        this.title = title;
        this.rows = rows;
        setup();
    }

    protected abstract void setup();

    public String getTitle() { return title; }
    public int getRows() { return rows; }
    public Map<Integer, Button> getButtons() { return buttons; }

    protected void setButton(int slot, Button button) {
        buttons.put(slot, button);
    }

    // ========== Inventory Security ==========

    /**
     * Enable full security — blocks all exploits.
     * Equivalent to adding all policies.
     */
    public Menu secure() {
        policies.addAll(EnumSet.allOf(InventoryPolicy.class));
        return this;
    }

    /**
     * Add specific inventory policies.
     *   menu.policy(InventoryPolicy.NO_SHIFT, InventoryPolicy.NO_DRAG);
     */
    public Menu policy(InventoryPolicy... policies) {
        this.policies.addAll(Arrays.asList(policies));
        return this;
    }

    public Set<InventoryPolicy> getPolicies() {
        return Collections.unmodifiableSet(policies);
    }

    public boolean hasPolicy(InventoryPolicy policy) {
        return policies.contains(policy);
    }

    // ========== Open ==========

    /**
     * Open this menu for a player.
     */
    public void open(Player player) {
        GuiManagerProvider.get().openMenu(player, this);
    }

    /**
     * Open this menu for a player, pushing current menu to navigation stack.
     * Use ctx.back() in buttons to return.
     */
    public void openWithHistory(Player player) {
        GuiManagerProvider.get().openMenu(player, this, true);
    }

    // ========== Lifecycle ==========

    /**
     * Called when menu is opened (override for custom logic).
     */
    public void onOpen(MenuView view) {}

    /**
     * Called when menu is closed (override for cleanup).
     */
    public void onClose(MenuView view) {}
}
