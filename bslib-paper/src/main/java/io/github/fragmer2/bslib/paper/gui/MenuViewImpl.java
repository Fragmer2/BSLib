package io.github.fragmer2.bslib.paper.gui;

import io.github.fragmer2.bslib.api.animation.Animation;
import io.github.fragmer2.bslib.api.animation.Frame;
import io.github.fragmer2.bslib.api.button.Button;
import io.github.fragmer2.bslib.api.button.ClickContext;
import io.github.fragmer2.bslib.api.menu.Menu;
import io.github.fragmer2.bslib.api.menu.MenuView;
import io.github.fragmer2.bslib.paper.BSLibPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Concrete implementation of {@link MenuView} for Paper servers.
 *
 * Each MenuViewImpl represents one player's open menu session. It manages:
 * - The Bukkit Inventory that the player sees
 * - Rendering buttons into ItemStacks
 * - Diffing rendered items to avoid unnecessary inventory updates
 * - Slot-level animations with frame timing
 * - Per-view state storage (key-value pairs)
 */
public class MenuViewImpl implements MenuView {
    private final Player player;
    private final Menu menu;
    private final Inventory inventory;
    private final Map<String, Object> state = new HashMap<>();
    private BukkitTask updateTask;
    private final Map<Integer, ItemStack> lastRendered = new HashMap<>();
    private final Map<Integer, AnimationState> animStates = new HashMap<>();

    private static class AnimationState {
        Animation anim;
        int currentFrame;
        long nextUpdate;
    }

    public MenuViewImpl(Player player, Menu menu) {
        this.player = player;
        this.menu = menu;
        this.inventory = Bukkit.createInventory(null, menu.getRows() * 9, menu.getTitle());
    }

    public Menu getMenu() {
        return menu;
    }

    /**
     * Refresh only dynamic and reactive button slots.
     * Uses item diffing to avoid unnecessary inventory updates.
     */
    public void refreshDynamicSlots() {
        String menuClass = menu.getClass().getSimpleName();
        menu.getButtons().forEach((slot, button) -> {
            // Static buttons: never refresh
            if (!button.isDynamic() && !button.isReactive()) return;

            // Reactive buttons: only refresh when watched value changed
            if (button.isReactive() && !button.hasValueChanged()) return;

            // Re-render with timing
            long start = System.nanoTime();
            ItemStack newItem = button.render(this);
            long elapsed = System.nanoTime() - start;

            // Track for GUI Inspector
            io.github.fragmer2.bslib.api.debug.GuiInspector.recordRender(menuClass, slot, elapsed);

            ItemStack oldItem = lastRendered.get(slot);
            if (!itemsEqual(newItem, oldItem)) {
                inventory.setItem(slot, newItem);
                lastRendered.put(slot, newItem);
            }
        });
    }

    private boolean itemsEqual(ItemStack a, ItemStack b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.isSimilar(b);
    }

    public void startAnimation(int slot, Animation anim) {
        AnimationState state = new AnimationState();
        state.anim = anim;
        state.currentFrame = 0;
        state.nextUpdate = System.currentTimeMillis() + anim.getFrames().get(0).getDuration() * 50;
        animStates.put(slot, state);
    }

    /**
     * Advance all active animations by one tick.
     * Uses Iterator to safely remove completed non-looping animations during iteration.
     */
    public void tickAnimations() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, AnimationState>> it = animStates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, AnimationState> entry = it.next();
            int slot = entry.getKey();
            AnimationState state = entry.getValue();
            if (now >= state.nextUpdate) {
                List<Frame> frames = state.anim.getFrames();
                int nextFrame = state.currentFrame + 1;
                if (nextFrame >= frames.size()) {
                    if (state.anim.isLoop()) {
                        nextFrame = 0;
                    } else {
                        it.remove(); // Safe removal via Iterator
                        continue;
                    }
                }
                state.currentFrame = nextFrame;
                Frame frame = frames.get(state.currentFrame);
                inventory.setItem(slot, frame.getItem());
                state.nextUpdate = now + frame.getDuration() * 50;
            }
        }
    }

    /** Stop all animations for this view. */
    public void stopAnimations() {
        animStates.clear();
    }

    @Override
    public Player getPlayer() { return player; }

    public void open() {
        render();
        player.openInventory(inventory);
        // Start animations for buttons that have them
        menu.getButtons().forEach((slot, button) -> {
            if (button.getAnimation() != null) {
                startAnimation(slot, button.getAnimation());
            }
        });
    }

    /**
     * Full render: clears the inventory and re-renders all buttons.
     */
    public void render() {
        inventory.clear();
        lastRendered.clear();
        menu.getButtons().forEach((slot, button) -> {
            ItemStack item = button.render(this);
            inventory.setItem(slot, item);
            lastRendered.put(slot, item);
        });
    }

    @Override
    public void update() { render(); }

    public void handleClick(int slot, InventoryClickEvent event) {
        Button button = menu.getButtons().get(slot);
        if (button == null) return;
        if (button.shouldCancel()) event.setCancelled(true);
        try {
            button.onClick(new ClickContext(player, event, this));
        } catch (Exception e) {
            player.sendMessage("\u00a7cAn error occurred while processing your click.");
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        stopAnimations();
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
        player.closeInventory();
    }

    @Override
    public void set(String key, Object value) { state.put(key, value); }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) { return (T) state.get(key); }
}
