package io.github.fragmer2.bslib.api.placeholder;

import org.bukkit.entity.Player;
import java.util.function.Function;

public interface PlaceholderRegistry {
    /**
     * Регистрирует новый плейсхолдер.
     * @param key ключ (без фигурных скобок)
     * @param replacer функция, принимающая игрока и возвращающая строку
     */
    void register(String key, Function<Player, String> replacer);

    /**
     * Применяет плейсхолдеры к тексту для указанного игрока.
     * @param player игрок
     * @param text исходный текст с {ключ}
     * @return текст с заменёнными плейсхолдерами
     */
    String apply(Player player, String text);
}