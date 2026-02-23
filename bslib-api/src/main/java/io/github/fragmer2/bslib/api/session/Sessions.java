package io.github.fragmer2.bslib.api.session;

import io.github.fragmer2.bslib.api.state.States;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Player session manager. Auto join-load, quit-save, cache.
 *
 * Access:
 *   Session s = Sessions.of(player);
 *   s.set("coins", 100);
 *   s.getInt("coins"); // 100
 *
 * Lifecycle hooks:
 *   Sessions.onJoin(session -> session.set("coins", loadFromDB(player)));
 *   Sessions.onQuit((player, session) -> saveToDB(player, session));
 *
 * All sessions are auto-created on join and auto-removed on quit.
 */
public final class Sessions implements Listener {
    private static final Map<UUID, Session> sessions = new ConcurrentHashMap<>();
    private static final List<Consumer<SessionContext>> joinHandlers = new CopyOnWriteArrayList<>();
    private static final List<BiConsumer<Player, Session>> quitHandlers = new CopyOnWriteArrayList<>();

    /**
     * State types that are automatically loaded on player join and unloaded on quit.
     * Register via Sessions.autoLoad(plugin, MyData.class).
     */
    private static final List<AutoStateEntry<?>> autoStates = new CopyOnWriteArrayList<>();

    private Sessions() {}

    // ========== Init (called by BSLib) ==========

    public static void init(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Sessions(), plugin);
    }

    // ========== Access ==========

    /**
     * Get or create session for a player.
     */
    public static Session of(Player player) {
        return sessions.computeIfAbsent(player.getUniqueId(), k -> new Session());
    }

    /**
     * Get session by UUID (may return null if offline).
     */
    public static Session get(UUID uuid) {
        return sessions.get(uuid);
    }

    /**
     * Check if a session exists.
     */
    public static boolean has(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    // ========== Auto-State (load/unload @State on join/quit) ==========

    /**
     * Register a @State class for automatic loading on player join and unloading on quit.
     * Data is loaded async from disk and available in the join callback.
     *
     * Usage:
     *   Sessions.autoLoad(plugin, PlayerData.class);
     *   // Now PlayerData is always ready when a player joins:
     *   PlayerData data = States.of(player, PlayerData.class);
     */
    public static <T> void autoLoad(Plugin plugin, Class<T> stateClass) {
        States.autoRegister(plugin, stateClass);
        autoStates.add(new AutoStateEntry<>(plugin, stateClass));
    }

    // ========== Lifecycle hooks ==========

    /**
     * Add a join handler. Multiple handlers supported.
     *   Sessions.onJoin(ctx -> {
     *       ctx.session().set("coins", db.loadCoins(ctx.player()));
     *   });
     */
    public static void onJoin(Consumer<SessionContext> handler) {
        joinHandlers.add(handler);
    }

    /**
     * Add a quit handler. Multiple handlers supported.
     *   Sessions.onQuit((player, session) -> {
     *       db.saveCoins(player, session.getInt("coins"));
     *   });
     */
    public static void onQuit(BiConsumer<Player, Session> handler) {
        quitHandlers.add(handler);
    }

    // ========== Events ==========

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Session session = new Session();
        sessions.put(player.getUniqueId(), session);
        SessionContext ctx = new SessionContext(player, session);

        // Auto-load all registered @State types for this player
        for (AutoStateEntry<?> entry : autoStates) {
            loadStateForPlayer(player, entry);
        }

        // Run join handlers
        for (Consumer<SessionContext> handler : joinHandlers) {
            try {
                handler.accept(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void loadStateForPlayer(Player player, AutoStateEntry<T> entry) {
        States.load(player, entry.stateClass, data -> {
            // State is loaded and cached — nothing else needed
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Session session = sessions.remove(player.getUniqueId());
        if (session != null) {
            for (BiConsumer<Player, Session> handler : quitHandlers) {
                try {
                    handler.accept(player, session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Cleanup reactive bindings for this player
        io.github.fragmer2.bslib.api.reactive.ReactiveBinding.destroyAll(player);
        // Cleanup GUI inspector
        io.github.fragmer2.bslib.api.debug.GuiInspector.clearPlayer(player);
        // Auto-save and unload all state for this player
        io.github.fragmer2.bslib.api.state.States.unloadAll(player);
    }

    /** Clear all sessions (called on disable). */
    public static void clear() {
        sessions.clear();
        joinHandlers.clear();
        quitHandlers.clear();
        autoStates.clear();
    }

    // ========== Context ==========

    public record SessionContext(Player player, Session session) {}

    // ========== Internal ==========

    private record AutoStateEntry<T>(Plugin plugin, Class<T> stateClass) {}
}
