package io.github.fragmer2.bslib.api.boss;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BossEntityTest {

    @Test
    void supportsGenericEntityEditsAndTickActions() {
        AtomicBoolean teleportCalled = new AtomicBoolean(false);
        Entity entity = fakeEntity(true, false, teleportCalled);

        AtomicBoolean onTickCalled = new AtomicBoolean(false);
        TestableBossEntity controller = new TestableBossEntity(entity);
        controller.onTick(ignored -> onTickCalled.set(true));
        controller.edit(ignored -> {});

        controller.teleport(fakeLocation());
        controller.runTick();

        assertFalse(controller.hasBossBar());
        assertTrue(controller.bossBarOptional().isEmpty());
        assertTrue(teleportCalled.get());
        assertTrue(onTickCalled.get());
        assertTrue(controller.subclassTickCalled.get());
    }

    @Test
    void updatesBossBarForLivingEntity() {
        LivingEntity livingEntity = fakeLivingEntity(50.0, 100.0);
        TestableBossEntity controller = new TestableBossEntity(livingEntity);
        controller.withBossBar(Component.text("Boss"), BossBar.Color.RED, BossBar.Overlay.PROGRESS);

        controller.runTick();

        assertTrue(controller.hasBossBar());
        assertTrue(controller.bossBarOptional().isPresent());
        assertEquals(0.5f, controller.bossBar().progress(), 0.0001f);
    }

    @Test
    void rejectsBossOnlyOperationsForNonLivingEntity() {
        Entity entity = fakeEntity(true, false, new AtomicBoolean());
        BossEntity controller = new BossEntity(entity);

        assertThrows(IllegalStateException.class, () -> controller.addPhase(new BossPhase(0.5, () -> {})));
        assertThrows(IllegalStateException.class,
                () -> controller.withBossBar(Component.text("Boss"), BossBar.Color.RED, BossBar.Overlay.PROGRESS));
    }

    private LivingEntity fakeLivingEntity(double health, double maxHealth) {
        AtomicReference<Double> currentHealth = new AtomicReference<>(health);
        return (LivingEntity) Proxy.newProxyInstance(
                LivingEntity.class.getClassLoader(),
                new Class[]{LivingEntity.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isValid" -> true;
                    case "isDead" -> false;
                    case "getHealth" -> currentHealth.get();
                    case "getMaxHealth" -> maxHealth;
                    case "teleport" -> true;
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> null;
                }
        );
    }

    private Entity fakeEntity(boolean valid, boolean dead, AtomicBoolean teleportCalled) {
        return (Entity) Proxy.newProxyInstance(
                Entity.class.getClassLoader(),
                new Class[]{Entity.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isValid" -> valid;
                    case "isDead" -> dead;
                    case "teleport" -> {
                        teleportCalled.set(true);
                        yield true;
                    }
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> null;
                }
        );
    }

    private Location fakeLocation() {
        return new Location(null, 0, 0, 0);
    }

    private static final class TestableBossEntity extends BossEntity {
        private final AtomicBoolean subclassTickCalled = new AtomicBoolean(false);

        private TestableBossEntity(Entity entity) {
            super(entity);
        }

        private void runTick() {
            tick();
        }

        @Override
        protected void handleTick(Entity entity) {
            subclassTickCalled.set(true);
        }

        @Override
        protected Iterable<? extends org.bukkit.entity.Player> onlinePlayers() {
            return java.util.List.of();
        }
    }
}
