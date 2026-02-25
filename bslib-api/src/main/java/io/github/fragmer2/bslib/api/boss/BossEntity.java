package io.github.fragmer2.bslib.api.boss;

import io.github.fragmer2.bslib.api.task.FrameworkTask;
import io.github.fragmer2.bslib.api.task.Tasks;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Flexible entity controller.
 *
 * <p>Can wrap any Bukkit {@link Entity} and expose lifecycle/tick hooks for plugin developers.
 * Boss-specific features (health phases + BossBar) are enabled only when wrapping a {@link LivingEntity}.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Entity controller API preview")
public class BossEntity {
    private final Entity entity;
    private BossBar bossBar;
    private final List<BossPhase> phases = new ArrayList<>();
    private final List<Consumer<Entity>> tickActions = new CopyOnWriteArrayList<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private FrameworkTask syncTask;
    private int phaseIndex = -1;

    /**
     * Create generic controller for any entity.
     */
    public BossEntity(Entity entity) {
        this.entity = Objects.requireNonNull(entity, "entity");
    }

    /**
     * Backward-compatible constructor that immediately enables bossbar support.
     */
    public BossEntity(LivingEntity entity, Component title, BossBar.Color color, BossBar.Overlay overlay) {
        this(entity);
        withBossBar(title, color, overlay);
    }

    /**
     * Enable bossbar tracking for living entities.
     */
    public BossEntity withBossBar(Component title, BossBar.Color color, BossBar.Overlay overlay) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalStateException("BossBar is supported only for LivingEntity");
        }
        this.bossBar = BossBar.bossBar(Objects.requireNonNull(title, "title"), 1.0f,
                Objects.requireNonNull(color, "color"), Objects.requireNonNull(overlay, "overlay"));
        return this;
    }

    /**
     * Add custom action executed each controller tick.
     */
    public BossEntity onTick(Consumer<Entity> action) {
        tickActions.add(Objects.requireNonNull(action, "action"));
        return this;
    }

    /**
     * Execute arbitrary entity edit immediately.
     */
    public BossEntity edit(Consumer<Entity> editor) {
        Objects.requireNonNull(editor, "editor").accept(entity);
        return this;
    }

    public BossEntity teleport(Location location) {
        entity.teleport(Objects.requireNonNull(location, "location"));
        return this;
    }

    public BossEntity addPhase(BossPhase phase) {
        requireLivingEntity();
        phases.add(Objects.requireNonNull(phase, "phase"));
        phases.sort(Comparator.comparingDouble(BossPhase::healthThreshold).reversed());
        return this;
    }

    public BossEntity start() {
        if (!started.compareAndSet(false, true)) {
            return this;
        }

        phaseIndex = -1;
        showBossBarForOnlinePlayers();
        syncTask = Tasks.sync().repeat(1).runTracked(this::tick);
        return this;
    }

    public void stop() {
        if (syncTask != null) {
            syncTask.cancel();
            syncTask = null;
        }
        hideBossBarForOnlinePlayers();
        started.set(false);
    }

    public Entity entity() {
        return entity;
    }

    public LivingEntity livingEntity() {
        return requireLivingEntity();
    }

    public boolean hasBossBar() {
        return bossBar != null;
    }

    public Optional<BossBar> bossBarOptional() {
        return Optional.ofNullable(bossBar);
    }

    public BossBar bossBar() {
        return bossBar;
    }

    /**
     * Extension point for inheritance-based customization.
     */
    protected void handleTick(Entity entity) {
        // extension point
    }

    protected void tick() {
        if (!entity.isValid() || entity.isDead()) {
            stop();
            return;
        }

        updateBossFeatures();

        for (Consumer<Entity> tickAction : tickActions) {
            tickAction.accept(entity);
        }
        handleTick(entity);
    }

    protected Iterable<? extends Player> onlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    private void updateBossFeatures() {
        if (bossBar == null || !(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        double maxHealth = livingEntity.getMaxHealth();
        if (maxHealth <= 0.0) {
            return;
        }

        float progress = (float) Math.max(0.0, Math.min(1.0, livingEntity.getHealth() / maxHealth));
        bossBar.progress(progress);
        applyPhases(progress);
    }

    private void applyPhases(float progress) {
        int nextIndex = phaseIndex + 1;
        while (nextIndex < phases.size() && progress <= phases.get(nextIndex).healthThreshold()) {
            phases.get(nextIndex).onEnter().run();
            phaseIndex = nextIndex;
            nextIndex = phaseIndex + 1;
        }
    }

    private void showBossBarForOnlinePlayers() {
        if (bossBar == null) {
            return;
        }
        for (Player player : onlinePlayers()) {
            player.showBossBar(bossBar);
        }
    }

    private void hideBossBarForOnlinePlayers() {
        if (bossBar == null) {
            return;
        }
        for (Player player : onlinePlayers()) {
            player.hideBossBar(bossBar);
        }
    }

    private LivingEntity requireLivingEntity() {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        throw new IllegalStateException("Operation is available only for LivingEntity");
    }
}
