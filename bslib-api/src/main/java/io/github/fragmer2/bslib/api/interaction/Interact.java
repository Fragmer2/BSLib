package io.github.fragmer2.bslib.api.interaction;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Fluent player interaction API.
 *
 * Right click with item:
 *   Interact.onRightClick()
 *       .with(Material.STICK)
 *       .handle(ctx -> ctx.player().sendMessage("Magic wand!"))
 *       .register(plugin);
 *
 * Left click:
 *   Interact.onLeftClick()
 *       .with(Material.BLAZE_ROD)
 *       .handle(ctx -> fireball(ctx.player()))
 *       .register(plugin);
 *
 * Any click with filter:
 *   Interact.onClick()
 *       .filter(ctx -> ctx.player().isSneaking())
 *       .handle(ctx -> { ... })
 *       .register(plugin);
 */
public final class Interact {
    private static final List<InteractionHandler> handlers = new CopyOnWriteArrayList<>();

    private Interact() {}

    public static InteractionBuilder onRightClick() {
        return new InteractionBuilder(ClickType.RIGHT);
    }

    public static InteractionBuilder onLeftClick() {
        return new InteractionBuilder(ClickType.LEFT);
    }

    public static InteractionBuilder onClick() {
        return new InteractionBuilder(ClickType.ANY);
    }

    /** Initialize the listener (called by BSLib). */
    public static void init(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InteractListener(), plugin);
    }

    public static void clear() {
        handlers.clear();
    }

    // ========== Types ==========

    public enum ClickType { LEFT, RIGHT, ANY }

    public static class InteractionContext {
        private final Player player;
        private final PlayerInteractEvent event;

        InteractionContext(Player player, PlayerInteractEvent event) {
            this.player = player;
            this.event = event;
        }

        public Player player() { return player; }
        public PlayerInteractEvent event() { return event; }
        public void cancel() { event.setCancelled(true); }
    }

    // ========== Builder ==========

    public static class InteractionBuilder {
        private final ClickType clickType;
        private Material material;
        private Predicate<InteractionContext> filter;
        private Consumer<InteractionContext> handler;
        private boolean cancelEvent = true;
        private String permission;

        InteractionBuilder(ClickType clickType) {
            this.clickType = clickType;
        }

        /** Only trigger when holding this material. */
        public InteractionBuilder with(Material material) {
            this.material = material;
            return this;
        }

        /** Custom filter. */
        public InteractionBuilder filter(Predicate<InteractionContext> filter) {
            this.filter = filter;
            return this;
        }

        /** Required permission. */
        public InteractionBuilder permission(String permission) {
            this.permission = permission;
            return this;
        }

        /** Don't cancel the event (default: cancel). */
        public InteractionBuilder allowDefault() {
            this.cancelEvent = false;
            return this;
        }

        /** Set the handler. */
        public InteractionBuilder handle(Consumer<InteractionContext> handler) {
            this.handler = handler;
            return this;
        }

        /** Register this interaction. */
        public void register(Plugin plugin) {
            handlers.add(new InteractionHandler(clickType, material, filter,
                    handler, cancelEvent, permission));
        }
    }

    // ========== Internal ==========

    private static class InteractionHandler {
        final ClickType clickType;
        final Material material;
        final Predicate<InteractionContext> filter;
        final Consumer<InteractionContext> handler;
        final boolean cancelEvent;
        final String permission;

        InteractionHandler(ClickType clickType, Material material,
                           Predicate<InteractionContext> filter,
                           Consumer<InteractionContext> handler,
                           boolean cancelEvent, String permission) {
            this.clickType = clickType;
            this.material = material;
            this.filter = filter;
            this.handler = handler;
            this.cancelEvent = cancelEvent;
            this.permission = permission;
        }

        boolean matches(PlayerInteractEvent event) {
            // Click type
            Action action = event.getAction();
            boolean rightClick = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
            boolean leftClick = action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;

            if (clickType == ClickType.RIGHT && !rightClick) return false;
            if (clickType == ClickType.LEFT && !leftClick) return false;
            if (clickType == ClickType.ANY && !rightClick && !leftClick) return false;

            // Material
            if (material != null) {
                var item = event.getItem();
                if (item == null || item.getType() != material) return false;
            }

            // Permission
            if (permission != null && !event.getPlayer().hasPermission(permission)) return false;

            return true;
        }
    }

    private static class InteractListener implements Listener {
        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            InteractionContext ctx = new InteractionContext(player, event);

            for (InteractionHandler handler : handlers) {
                if (!handler.matches(event)) continue;
                if (handler.filter != null && !handler.filter.test(ctx)) continue;

                if (handler.cancelEvent) event.setCancelled(true);
                try {
                    handler.handler.accept(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
