package io.github.fragmer2.bslib.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Fluent listener API with auto-cleanup.
 *
 * Basic:
 *   Events.listen(PlayerJoinEvent.class, e -> e.getPlayer().sendMessage("Hi!"))
 *         .register(plugin);
 *
 * With filter:
 *   Events.listen(PlayerMoveEvent.class, e -> handleMove(e))
 *         .filter(e -> e.getPlayer().getWorld().getName().equals("arena"))
 *         .register(plugin);
 *
 * Auto-expire:
 *   Events.listen(BlockBreakEvent.class, e -> { ... })
 *         .expireAfter(100) // unregister after 100 calls
 *         .register(plugin);
 *
 * Player-scoped (auto-remove when player quits):
 *   Events.listen(PlayerMoveEvent.class, e -> { ... })
 *         .forPlayer(player)
 *         .register(plugin);
 *
 * Module-owned:
 *   Events.listen(PlayerJoinEvent.class, e -> { ... })
 *         .register(plugin);
 *   Events.unregisterAll(plugin); // cleanup all listeners for this plugin
 */
public final class Events {
    private static final List<ManagedListener<?>> allListeners = new CopyOnWriteArrayList<>();

    private Events() {}

    public static <T extends Event> ListenerBuilder<T> listen(Class<T> eventType, Consumer<T> handler) {
        return new ListenerBuilder<>(eventType, handler);
    }

    /**
     * Unregister all managed listeners for a plugin/owner.
     */
    public static void unregisterAll(Plugin plugin) {
        allListeners.removeIf(l -> {
            if (l.plugin == plugin) {
                l.unregister();
                return true;
            }
            return false;
        });
    }

    public static void clear() {
        for (ManagedListener<?> l : allListeners) {
            l.unregister();
        }
        allListeners.clear();
    }

    // ========== Builder ==========

    public static class ListenerBuilder<T extends Event> {
        private final Class<T> eventType;
        private final Consumer<T> handler;
        private EventPriority priority = EventPriority.NORMAL;
        private boolean ignoreCancelled = false;
        private Predicate<T> filter;
        private Player forPlayer;
        private int expireAfter = -1;

        ListenerBuilder(Class<T> eventType, Consumer<T> handler) {
            this.eventType = eventType;
            this.handler = handler;
        }

        public ListenerBuilder<T> priority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        public ListenerBuilder<T> ignoreCancelled() {
            this.ignoreCancelled = true;
            return this;
        }

        public ListenerBuilder<T> filter(Predicate<T> filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Only fire for a specific player. Auto-unregisters when player quits.
         */
        public ListenerBuilder<T> forPlayer(Player player) {
            this.forPlayer = player;
            return this;
        }

        /**
         * Auto-unregister after N invocations.
         */
        public ListenerBuilder<T> expireAfter(int times) {
            this.expireAfter = times;
            return this;
        }

        /**
         * Register this listener.
         */
        public ManagedListener<T> register(Plugin plugin) {
            ManagedListener<T> managed = new ManagedListener<>(plugin, eventType, handler,
                    priority, ignoreCancelled, filter, forPlayer, expireAfter);
            managed.register();
            allListeners.add(managed);
            return managed;
        }
    }

    // ========== Managed Listener ==========

    public static class ManagedListener<T extends Event> implements Listener {
        final Plugin plugin;
        private final Class<T> eventType;
        private final Consumer<T> handler;
        private final EventPriority priority;
        private final boolean ignoreCancelled;
        private final Predicate<T> filter;
        private final Player forPlayer;
        private int remaining;
        private boolean registered = false;

        ManagedListener(Plugin plugin, Class<T> eventType, Consumer<T> handler,
                        EventPriority priority, boolean ignoreCancelled,
                        Predicate<T> filter, Player forPlayer, int expireAfter) {
            this.plugin = plugin;
            this.eventType = eventType;
            this.handler = handler;
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
            this.filter = filter;
            this.forPlayer = forPlayer;
            this.remaining = expireAfter;
        }

        @SuppressWarnings("unchecked")
        void register() {
            if (registered) return;
            Bukkit.getPluginManager().registerEvent(eventType, this, priority,
                    (listener, event) -> {
                        if (!eventType.isInstance(event)) return;
                        T typed = (T) event;

                        // Cancelled check
                        if (ignoreCancelled && event instanceof Cancellable c && c.isCancelled()) return;

                        // Player filter
                        if (forPlayer != null) {
                            if (typed instanceof PlayerEvent pe) {
                                if (!pe.getPlayer().getUniqueId().equals(forPlayer.getUniqueId())) return;
                            }
                        }

                        // Custom filter
                        if (filter != null && !filter.test(typed)) return;

                        // Expiry check
                        if (remaining == 0) {
                            unregister();
                            return;
                        }
                        if (remaining > 0) remaining--;

                        handler.accept(typed);
                    },
                    plugin, ignoreCancelled);
            registered = true;

            // Auto-unregister when player quits
            if (forPlayer != null) {
                Bukkit.getPluginManager().registerEvent(
                        org.bukkit.event.player.PlayerQuitEvent.class, this, EventPriority.MONITOR,
                        (listener, event) -> {
                            if (event instanceof org.bukkit.event.player.PlayerQuitEvent quit) {
                                if (quit.getPlayer().getUniqueId().equals(forPlayer.getUniqueId())) {
                                    unregister();
                                }
                            }
                        }, plugin, false);
            }
        }

        public void unregister() {
            if (!registered) return;
            HandlerList.unregisterAll(this);
            registered = false;
            allListeners.remove(this);
        }

        public boolean isRegistered() {
            return registered;
        }
    }
}
