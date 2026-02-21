package io.github.fragmer2.bslib.api;

import io.github.fragmer2.bslib.api.menu.Menu;
import org.bukkit.entity.Player;

public class GuiManagerProvider {
    private static GuiManager instance;

    public static void setInstance(GuiManager manager) {
        instance = manager;
    }

    public static GuiManager get() {
        if (instance == null) {
            throw new IllegalStateException("GuiManager not initialized. Is BSLib loaded?");
        }
        return instance;
    }

    public interface GuiManager {
        void openMenu(Player player, Menu menu);

        /**
         * Open menu and push current menu to navigation stack.
         */
        void openMenu(Player player, Menu menu, boolean addToHistory);

        void closeMenu(Player player);

        /**
         * Go back to previous menu in navigation stack.
         */
        void back(Player player);
    }
}
