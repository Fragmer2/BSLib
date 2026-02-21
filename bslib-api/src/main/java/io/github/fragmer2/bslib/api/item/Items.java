package io.github.fragmer2.bslib.api.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Item serialization API — store items in databases as strings.
 *
 * Serialize:
 *   String data = Items.serialize(itemStack);
 *   // store in database
 *
 * Deserialize:
 *   ItemStack item = Items.deserialize(data);
 *   // give to player
 *
 * Array support:
 *   String data = Items.serializeArray(player.getInventory().getContents());
 *   ItemStack[] items = Items.deserializeArray(data);
 */
public final class Items {

    private Items() {}

    /**
     * Serialize an ItemStack to a Base64 string.
     */
    public static String serialize(ItemStack item) {
        if (item == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos)) {
            oos.writeObject(item);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ItemStack", e);
        }
    }

    /**
     * Deserialize an ItemStack from a Base64 string.
     */
    public static ItemStack deserialize(String data) {
        if (data == null || data.isEmpty()) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream ois = new BukkitObjectInputStream(bais)) {
            return (ItemStack) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ItemStack", e);
        }
    }

    /**
     * Serialize an array of ItemStacks (e.g., inventory contents).
     */
    public static String serializeArray(ItemStack[] items) {
        if (items == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos)) {
            oos.writeInt(items.length);
            for (ItemStack item : items) {
                oos.writeObject(item);
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ItemStack array", e);
        }
    }

    /**
     * Deserialize an array of ItemStacks.
     */
    public static ItemStack[] deserializeArray(String data) {
        if (data == null || data.isEmpty()) return new ItemStack[0];
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream ois = new BukkitObjectInputStream(bais)) {
            int size = ois.readInt();
            ItemStack[] items = new ItemStack[size];
            for (int i = 0; i < size; i++) {
                items[i] = (ItemStack) ois.readObject();
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ItemStack array", e);
        }
    }
}
