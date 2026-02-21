package io.github.fragmer2.bslib.api.button;

import io.github.fragmer2.bslib.api.menu.MenuView;
import org.bukkit.entity.Player;
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

    public Player player() { return player; }
    public InventoryClickEvent event() { return event; }
    public MenuView view() { return view; }
    public void close() { view.close(); }
    public void update() { view.update(); }
    public void back() {
        io.github.fragmer2.bslib.api.GuiManagerProvider.get().back(player);
    }
}

