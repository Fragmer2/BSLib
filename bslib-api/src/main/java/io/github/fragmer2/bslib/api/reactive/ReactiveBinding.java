package io.github.fragmer2.bslib.api.reactive;

import io.github.fragmer2.bslib.api.task.Tasks;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Auto-binding for Minecraft visual elements to Reactive values.
 *
 * ===== Scoreboard =====
 *   ReactiveBinding.scoreboard(player, "My Server")
 *       .line(0, coins.map(c -> "§6Coins: §f" + c))
 *       .line(1, kills.map(k -> "§cKills: §f" + k))
 *       .line(2, Reactive.of("§7----------"))     // static line
 *       .line(3, rank)
 *       .build(plugin);
 *
 * ===== BossBar =====
 *   ReactiveBinding.bossbar(player, BossBar.Color.RED)
 *       .title(hp.map(h -> "<red>Boss HP: " + h + "%"))
 *       .progress(hp.map(h -> h / 100.0f))
 *       .build(plugin);
 *
 * ===== ActionBar =====
 *   ReactiveBinding.actionbar(player)
 *       .text(balance.map(b -> "<gold>" + b + " coins"))
 *       .build(plugin);
 *
 * All bindings auto-update when the bound Reactive changes.
 * Call .destroy() to stop and clean up.
 */
public final class ReactiveBinding {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Map<UUID, List<Destroyable>> playerBindings = new ConcurrentHashMap<>();

    private ReactiveBinding() {}

    // ========== Factories ==========

    public static ScoreboardBuilder scoreboard(Player player, String title) {
        return new ScoreboardBuilder(player, title);
    }

    public static BossBarBuilder bossbar(Player player, BossBar.Color color) {
        return new BossBarBuilder(player, color);
    }

    public static ActionBarBuilder actionbar(Player player) {
        return new ActionBarBuilder(player);
    }

    /** Destroy all bindings for a player (call on quit). */
    public static void destroyAll(Player player) {
        List<Destroyable> bindings = playerBindings.remove(player.getUniqueId());
        if (bindings != null) {
            bindings.forEach(Destroyable::destroy);
        }
    }

    /** Destroy all bindings globally (call on disable). */
    public static void clear() {
        playerBindings.values().forEach(list -> list.forEach(Destroyable::destroy));
        playerBindings.clear();
    }

    public static int activePlayerCount() {
        return playerBindings.size();
    }

    public static int activeBindingCount() {
        return playerBindings.values().stream().mapToInt(List::size).sum();
    }

    private static void track(Player player, Destroyable binding) {
        playerBindings.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(binding);
    }

    // ========== Interfaces ==========

    public interface Destroyable {
        void destroy();
    }

    // ========== Scoreboard Builder ==========

    public static class ScoreboardBuilder {
        private final Player player;
        private final String title;
        private final Map<Integer, Reactive<String>> lines = new TreeMap<>();

        ScoreboardBuilder(Player player, String title) {
            this.player = player;
            this.title = title;
        }

        /**
         * Bind a scoreboard line to a Reactive.
         * Line 0 = top, higher numbers = lower on scoreboard.
         */
        public ScoreboardBuilder line(int index, Reactive<String> value) {
            lines.put(index, value);
            return this;
        }

        /** Static line shortcut. */
        public ScoreboardBuilder line(int index, String staticText) {
            lines.put(index, Reactive.of(staticText));
            return this;
        }

        public Destroyable build(Plugin plugin) {
            ScoreboardManager manager = org.bukkit.Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Objective obj = board.registerNewObjective("bslib_sb", Criteria.DUMMY,
                    MM.deserialize(title));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            int maxScore = lines.size();
            Map<Integer, Team> teams = new HashMap<>();
            Map<Integer, String> entries = new HashMap<>();

            // Create teams + entries
            for (Map.Entry<Integer, Reactive<String>> entry : lines.entrySet()) {
                int idx = entry.getKey();
                int score = maxScore - idx;
                String entryName = "§" + Integer.toHexString(idx) + "§r"; // unique invisible entry
                Team team = board.registerNewTeam("line_" + idx);
                team.addEntry(entryName);
                obj.getScore(entryName).setScore(score);
                teams.put(idx, team);
                entries.put(idx, entryName);

                // Set initial value
                team.prefix(MM.deserialize(entry.getValue().get()));
            }

            player.setScoreboard(board);

            // Subscribe to changes
            List<Subscription> subscriptions = new ArrayList<>();
            for (Map.Entry<Integer, Reactive<String>> entry : lines.entrySet()) {
                int idx = entry.getKey();
                Reactive<String> reactive = entry.getValue();
                Team team = teams.get(idx);

                subscriptions.add(reactive.subscribeSet(val -> {
                    if (player.isOnline()) {
                        Tasks.sync().run(() -> team.prefix(MM.deserialize(val)));
                    }
                }));
            }

            Destroyable binding = () -> {
                subscriptions.forEach(Subscription::unsubscribe);
                if (player.isOnline()) {
                    player.setScoreboard(manager.getMainScoreboard());
                }
            };
            track(player, binding);
            return binding;
        }
    }

    // ========== BossBar Builder ==========

    public static class BossBarBuilder {
        private final Player player;
        private final BossBar.Color color;
        private Reactive<String> title;
        private Reactive<Float> progress;
        private BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        BossBarBuilder(Player player, BossBar.Color color) {
            this.player = player;
            this.color = color;
        }

        public BossBarBuilder title(Reactive<String> title) {
            this.title = title;
            return this;
        }

        public BossBarBuilder title(String staticTitle) {
            this.title = Reactive.of(staticTitle);
            return this;
        }

        public BossBarBuilder progress(Reactive<Float> progress) {
            this.progress = progress;
            return this;
        }

        public BossBarBuilder progress(float staticProgress) {
            this.progress = Reactive.of(staticProgress);
            return this;
        }

        public BossBarBuilder overlay(BossBar.Overlay overlay) {
            this.overlay = overlay;
            return this;
        }

        public Destroyable build(Plugin plugin) {
            String initialTitle = title != null ? title.get() : "";
            float initialProgress = progress != null ? Math.max(0, Math.min(1, progress.get())) : 1.0f;

            BossBar bar = BossBar.bossBar(
                    MM.deserialize(initialTitle),
                    initialProgress,
                    color,
                    overlay
            );
            player.showBossBar(bar);

            List<Subscription> subscriptions = new ArrayList<>();
            if (title != null) {
                subscriptions.add(title.subscribeSet(val -> {
                    if (player.isOnline()) bar.name(MM.deserialize(val));
                }));
            }
            if (progress != null) {
                subscriptions.add(progress.subscribeSet(val -> {
                    if (player.isOnline()) bar.progress(Math.max(0, Math.min(1, val)));
                }));
            }

            Destroyable binding = () -> {
                subscriptions.forEach(Subscription::unsubscribe);
                if (player.isOnline()) player.hideBossBar(bar);
            };
            track(player, binding);
            return binding;
        }
    }

    // ========== ActionBar Builder ==========

    public static class ActionBarBuilder {
        private final Player player;
        private Reactive<String> text;
        private long intervalTicks = 20; // how often to resend (actionbar fades after ~2s)

        ActionBarBuilder(Player player) {
            this.player = player;
        }

        public ActionBarBuilder text(Reactive<String> text) {
            this.text = text;
            return this;
        }

        public ActionBarBuilder text(String staticText) {
            this.text = Reactive.of(staticText);
            return this;
        }

        /** How often to resend (default: 20 ticks = 1 second). */
        public ActionBarBuilder interval(long ticks) {
            this.intervalTicks = ticks;
            return this;
        }

        public Destroyable build(Plugin plugin) {
            if (text == null) throw new IllegalStateException("ActionBar text not set");

            // ActionBar needs periodic resend because it fades
            BukkitTask task = Tasks.sync().repeat(intervalTicks).run(() -> {
                if (player.isOnline()) {
                    player.sendActionBar(MM.deserialize(text.get()));
                }
            });

            Destroyable binding = task::cancel;
            track(player, binding);
            return binding;
        }
    }
}
