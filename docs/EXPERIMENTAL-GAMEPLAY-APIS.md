# Experimental Gameplay APIs (Boss / Cinematic / Particles / Messaging)

This document describes the newly added **experimental** APIs intended to help plugin authors quickly build gameplay systems while keeping code flexible.

> All APIs below are marked experimental and may evolve.

## 1) BossEntity (Generic Entity Controller)

`BossEntity` is a generic wrapper over Bukkit `Entity`.

- Works with **any** entity (`Entity`) for common operations.
- Boss-specific features are available only for `LivingEntity`.

### Core capabilities

- `new BossEntity(entity)` — generic controller.
- `onTick(Consumer<Entity>)` — register composable per-tick actions.
- `edit(Consumer<Entity>)` — immediate entity mutation hook.
- `teleport(Location)` — convenience method.
- `start()` / `stop()` — lifecycle managed by `Tasks` scheduler.

### Boss-specific capabilities (LivingEntity only)

- `withBossBar(title, color, overlay)` — attach BossBar.
- `addPhase(BossPhase)` — phase transitions based on health ratio.
- `hasBossBar()` / `bossBarOptional()` — safe access to optional BossBar.

### Example

```java
BossEntity controller = new BossEntity(entity)
    .onTick(e -> {
        // custom AI or effects
    })
    .edit(e -> e.setCustomName(net.kyori.adventure.text.Component.text("Cinematic Mob")));

controller.start();
```

For a living boss:

```java
BossEntity boss = new BossEntity(zombie)
    .withBossBar(Component.text("Worldbreaker"), BossBar.Color.RED, BossBar.Overlay.PROGRESS)
    .addPhase(new BossPhase(0.5, () -> {
        // phase 2 behavior
    }));

boss.start();
```

## 2) Timeline (Cinematic Sequence MVP)

`Timeline` schedules actions by ticks/seconds via `Tasks`.

### API

- `Timeline.create()`
- `atTicks(long ticks, Runnable action)`
- `atSeconds(double seconds, Runnable action)`
- `play()`

### Example

```java
Timeline.create()
    .atSeconds(1, () -> player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f))
    .atSeconds(2, () -> player.sendMessage("<red>Everything starts now"))
    .play();
```

## 3) ParticlePattern / Particles

Declarative particle shape builder.

### Built-in patterns

- `Particles.circle(center, radius)`
- `Particles.spiral(center, radius, height)`
- `Particles.beam(from, to)`
- `Particles.single(location)`

### Fluent options

- `.particle(Particle)`
- `.count(int)`
- `.offset(x, y, z)`
- `.speed(double)`
- `.color(Color)` (for DUST)
- `.size(float)` (must be > 0)

### Playback

- `play()` — world broadcast.
- `play(player)` — per-player only.

## 4) LocalRemoteBridge

In-memory implementation of `RemoteBridge`, good for single-server/dev setups.

### Features

- `subscribe(channel, consumer)`
- `publish(channel, payload)`
- `unsubscribe(channel, consumer)`
- `clearChannel(channel)`

Delivery is isolated: one failing subscriber does not block others.

## 5) Item alias

`Item.customModelData(int)` was added as an ergonomic alias to `Item.model(int)`.

---

## Notes for plugin developers

- These APIs are intentionally lightweight and composable.
- Prefer composition (`onTick`, `Timeline`, `Particles`) over hardcoded game modes.
- Treat experimental APIs as subject to minor signature/behavior changes in next versions.
