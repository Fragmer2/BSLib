package io.github.fragmer2.bslib.api.item;

import io.github.fragmer2.bslib.api.placeholder.Placeholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Item {
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    private final Material material;
    private int amount = 1;
    private String displayName;
    private final List<String> lore = new ArrayList<>();
    private boolean glow = false;
    private boolean hideFlags = false;
    private Integer customModelData = null;
    private boolean unbreakable = false;
    private final List<EnchantEntry> enchantments = new ArrayList<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private String skullTexture; // для голов игроков

    private Item(Material material) {
        this.material = material;
    }

    public static Item of(Material material) {
        return new Item(material);
    }

    // ========== Базовые настройки ==========
    public Item amount(int amount) {
        this.amount = amount;
        return this;
    }

    public Item name(String text) {
        this.displayName = text;
        return this;
    }

    public Item lore(String... lines) {
        this.lore.addAll(Arrays.asList(lines));
        return this;
    }

    public Item lore(List<String> lines) {
        this.lore.addAll(lines);
        return this;
    }

    // ========== Эффекты ==========
    public Item glow() {
        this.glow = true;
        return this;
    }

    public Item enchant(Enchantment enchantment, int level) {
        this.enchantments.add(new EnchantEntry(enchantment, level));
        return this;
    }

    public Item unbreakable() {
        this.unbreakable = true;
        return this;
    }

    public Item addFlags(ItemFlag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public Item hideFlags() {
        return addFlags(ItemFlag.values());
    }

    // ========== Модель и текстуры ==========
    public Item model(int id) {
        this.customModelData = id;
        return this;
    }

    /**
     * Устанавливает текстуру головы игрока (по никнейму).
     */
    public Item skullOwner(String playerName) {
        this.skullTexture = "player:" + playerName;
        return this;
    }

    /**
     * Устанавливает кастомную текстуру головы (base64).
     */
    public Item skullTexture(String base64) {
        this.skullTexture = base64;
        return this;
    }

    // ========== Построение ==========
    public ItemStack build() {
        return build(null);
    }

    public ItemStack build(Player player) {
        ItemStack item = new ItemStack(material, amount);

        // Обработка голов
        if (material == Material.PLAYER_HEAD && skullTexture != null) {
            item = applySkullTexture(item, skullTexture);
        }

        ItemMeta meta = item.getItemMeta();

        // Имя
        if (displayName != null) {
            String processed = (player != null) ? Placeholders.apply(player, displayName) : displayName;
            meta.displayName(LEGACY_SERIALIZER.deserialize(processed));
        }

        // Lore
        if (!lore.isEmpty()) {
            List<Component> loreComponents = lore.stream()
                .map(line -> (player != null) ? Placeholders.apply(player, line) : line)
                .map(LEGACY_SERIALIZER::deserialize)
                .collect(Collectors.toList());
            meta.lore(loreComponents);
        }

        // Зачарования
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        for (EnchantEntry e : enchantments) {
            meta.addEnchant(e.enchantment, e.level, true);
        }

        // Флаги
        if (!flags.isEmpty()) {
            meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        }

        // Unbreakable
        if (unbreakable) {
            meta.setUnbreakable(true);
        }

        // Model data
        if (customModelData != null) {
            meta.setCustomModelData(customModelData);
        }

        item.setItemMeta(meta);
        return item;
    }

    // Вспомогательный метод для установки текстуры головы
    private ItemStack applySkullTexture(ItemStack item, String texture) {
        if (!(item.getItemMeta() instanceof SkullMeta)) return item;
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (texture.startsWith("player:")) {
            String name = texture.substring(7);
            org.bukkit.OfflinePlayer offlinePlayer = org.bukkit.Bukkit.getOfflinePlayer(name);
            meta.setOwningPlayer(offlinePlayer);
        } else {
            // Кастомная текстура (base64) через профиль
            com.destroystokyo.paper.profile.PlayerProfile profile = org.bukkit.Bukkit.createProfile(null, null); // два null
            profile.getProperties().add(new com.destroystokyo.paper.profile.ProfileProperty("textures", texture));
            meta.setPlayerProfile(profile);
        }
        item.setItemMeta(meta);
        return item;
    }

    // Внутренний класс для хранения зачарований
    private static class EnchantEntry {
        Enchantment enchantment;
        int level;
        EnchantEntry(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }
    }
}