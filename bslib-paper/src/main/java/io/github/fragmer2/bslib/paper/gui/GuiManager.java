package io.github.fragmer2.bslib.paper.gui;

import io.github.fragmer2.bslib.api.menu.InventoryPolicy;
import io.github.fragmer2.bslib.api.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central GUI manager that handles menu lifecycle, event routing, and refresh polling.
 *
 * Responsibilities:
 * - Tracks all open menu views per player
 * - Handles inventory click/drag/close events and routes them to the correct menu
 * - Polls reactive menus and dynamic buttons for updates every 2 ticks
 * - Manages menu navigation history (back button support)
 * - Cleans up resources on player quit
 */
public class GuiManager implements Listener {
    private final Map<UUID, MenuViewImpl> openViews = new ConcurrentHashMap<>();
    private final Map<UUID, Deque<Menu>> history = new ConcurrentHashMap<>();
    private final Set<UUID> skipCloseEvent = ConcurrentHashMap.newKeySet();
    private BukkitTask refreshTask;
    private JavaPlugin plugin;

    public void register(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startRefreshTask(plugin);
    }

    private void startRefreshTask(JavaPlugin plugin) {
        refreshTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Iterate over a snapshot to avoid ConcurrentModificationException
            for (MenuViewImpl view : new ArrayList<>(openViews.values())) {
                try {
                    // Check DeclarativeMenu bindings → full re-render if changed
                    if (io.github.fragmer2.bslib.api.menu.ReactiveMenu.isDeclarative(view.getMenu())) {
                        if (io.github.fragmer2.bslib.api.menu.ReactiveMenu.checkRerender(view.getMenu())) {
                            view.render(); // full re-render (buttons changed)
                        }
                    }
                    view.refreshDynamicSlots();
                    view.tickAnimations();
                } catch (Exception e) {
                    plugin.getLogger().warning("Error refreshing menu for " +
                            view.getPlayer().getName() + ": " + e.getMessage());
                }
            }
        }, 2L, 2L);
    }

    public void cancel() {
        if (refreshTask != null) refreshTask.cancel();
        // Close all open menus on shutdown
        for (Map.Entry<UUID, MenuViewImpl> entry : openViews.entrySet()) {
            try {
                MenuViewImpl view = entry.getValue();
                view.getMenu().onClose(view);
                view.close();
            } catch (Exception ignored) {}
        }
        openViews.clear();
        history.clear();
        skipCloseEvent.clear();
    }

    public void openMenu(Player player, Menu menu) {
        openMenu(player, menu, false);
    }

    public void openMenu(Player player, Menu menu, boolean addToHistory) {
        UUID uuid = player.getUniqueId();

        if (addToHistory) {
            MenuViewImpl current = openViews.get(uuid);
            if (current != null) {
                history.computeIfAbsent(uuid, k -> new ArrayDeque<>()).push(current.getMenu());
            }
        }

        closeViewSilently(player);

        MenuViewImpl view = new MenuViewImpl(player, menu);
        openViews.put(uuid, view);
        view.open();
        menu.onOpen(view);
    }

    public void back(Player player) {
        UUID uuid = player.getUniqueId();
        Deque<Menu> stack = history.get(uuid);
        if (stack != null && !stack.isEmpty()) {
            Menu previous = stack.pop();
            closeViewSilently(player);
            MenuViewImpl view = new MenuViewImpl(player, previous);
            openViews.put(uuid, view);
            view.open();
            previous.onOpen(view);
        } else {
            closeMenu(player);
        }
    }

    public Menu getCurrentMenu(Player player) {
        MenuViewImpl view = openViews.get(player.getUniqueId());
        return view != null ? view.getMenu() : null;
    }

    /** Check if a player currently has a BSLib menu open. */
    public boolean hasOpenMenu(Player player) {
        return openViews.containsKey(player.getUniqueId());
    }

    public void closeMenu(Player player) {
        UUID uuid = player.getUniqueId();
        MenuViewImpl view = openViews.remove(uuid);
        if (view != null) {
            skipCloseEvent.add(uuid);
            view.getMenu().onClose(view);
            view.close();
            Bukkit.getScheduler().runTask(plugin, () -> skipCloseEvent.remove(uuid));
        }
        history.remove(uuid);
    }

    private void closeViewSilently(Player player) {
        UUID uuid = player.getUniqueId();
        MenuViewImpl view = openViews.remove(uuid);
        if (view != null) {
            skipCloseEvent.add(uuid);
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(plugin, () -> skipCloseEvent.remove(uuid), 1L);
        }
    }

    // ========== Events ==========

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        MenuViewImpl view = openViews.get(player.getUniqueId());
        if (view == null) return;

        Menu menu = view.getMenu();

        // --- Bottom inventory (player's own) ---
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            // These ALWAYS blocked in menus (items would move into menu):
            if (event.isShiftClick() || event.getClick() == ClickType.DOUBLE_CLICK
                    || event.getClick() == ClickType.NUMBER_KEY) {
                event.setCancelled(true);
                return;
            }
            // Normal bottom-inv clicks: allow unless NO_BOTTOM_INV policy
            if (menu.hasPolicy(InventoryPolicy.NO_BOTTOM_INV)) {
                event.setCancelled(true);
            }
            return;
        }

        // --- Top inventory (menu area) ---
        // GUI Inspector intercept (dev mode)
        io.github.fragmer2.bslib.api.button.Button button = menu.getButtons().get(event.getSlot());
        if (io.github.fragmer2.bslib.api.debug.GuiInspector.isEnabled(player)) {
            event.setCancelled(true);
            io.github.fragmer2.bslib.api.debug.GuiInspector.inspect(player, menu, event.getSlot(), button, view);
            return;
        }

        // Empty slot → always cancel (prevents placing items in menu)
        if (button == null) {
            event.setCancelled(true);
            return;
        }

        // Button exists → delegate (button.shouldCancel() decides)
        view.handleClick(event.getSlot(), event);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        MenuViewImpl view = openViews.get(player.getUniqueId());
        if (view == null) return;

        int topSize = event.getView().getTopInventory().getSize();
        boolean affectsTop = event.getRawSlots().stream().anyMatch(slot -> slot < topSize);

        if (affectsTop) {
            // Drag into menu area — always cancel
            event.setCancelled(true);
        } else if (view.getMenu().hasPolicy(InventoryPolicy.NO_DRAG)) {
            // Drag in bottom inv — only cancel if policy set
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        UUID uuid = player.getUniqueId();

        if (skipCloseEvent.contains(uuid)) return;

        MenuViewImpl view = openViews.remove(uuid);
        if (view != null) {
            view.getMenu().onClose(view);
        }
        history.remove(uuid);
    }

    /**
     * Clean up all GUI state when a player quits.
     * Prevents memory leaks from abandoned menu views and history stacks.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        MenuViewImpl view = openViews.remove(uuid);
        if (view != null) {
            try {
                view.getMenu().onClose(view);
            } catch (Exception ignored) {}
        }
        history.remove(uuid);
        skipCloseEvent.remove(uuid);
    }
}
