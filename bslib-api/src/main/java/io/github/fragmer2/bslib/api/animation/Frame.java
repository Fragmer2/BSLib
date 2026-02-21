package io.github.fragmer2.bslib.api.animation;

import org.bukkit.inventory.ItemStack;

public class Frame {
    private final ItemStack item;
    private final long duration; // в тиках

    public Frame(ItemStack item, long duration) {
        this.item = item;
        this.duration = duration;
    }

    public ItemStack getItem() { return item; }
    public long getDuration() { return duration; }
}