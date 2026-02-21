package io.github.fragmer2.bslib.api.menu;

import org.bukkit.entity.Player;

public interface MenuView {
    Player getPlayer();
    void update();
    void close();
    void set(String key, Object value);
    <T> T get(String key);
}