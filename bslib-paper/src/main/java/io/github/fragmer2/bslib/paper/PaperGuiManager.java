package io.github.fragmer2.bslib.paper;

import io.github.fragmer2.bslib.api.GuiManagerProvider;
import io.github.fragmer2.bslib.api.menu.Menu;
import io.github.fragmer2.bslib.paper.gui.GuiManager;
import org.bukkit.entity.Player;

public class PaperGuiManager implements GuiManagerProvider.GuiManager {
    private final GuiManager realManager;

    public PaperGuiManager(GuiManager realManager) {
        this.realManager = realManager;
    }

    @Override
    public void openMenu(Player player, Menu menu) {
        realManager.openMenu(player, menu);
    }

    @Override
    public void openMenu(Player player, Menu menu, boolean addToHistory) {
        realManager.openMenu(player, menu, addToHistory);
    }

    @Override
    public void closeMenu(Player player) {
        realManager.closeMenu(player);
    }

    @Override
    public void back(Player player) {
        realManager.back(player);
    }
}
