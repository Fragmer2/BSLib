# BSLib Developer Guide

## 1) What is BSLib
BSLib is a framework layer for Minecraft plugin development on Paper. It gives you production-ready primitives for lifecycle, services, modules, tasks, reactive state, messaging, and capability contracts.

### Why use it instead of raw Paper API?
Raw Paper is powerful, but plugin teams repeatedly rebuild the same infrastructure:
- bootstrapping and dependency wiring,
- task lifecycle cleanup,
- module startup order,
- cross-plugin communication,
- state propagation.

BSLib standardizes these patterns so you can focus on gameplay/business logic.

### Design philosophy
- **Practical first**: APIs should be easy to use in real plugins.
- **Lifecycle-safe**: registration and teardown are explicit.
- **Owner-aware**: shared registries support plugin ownership.
- **Composable**: systems work together (services + modules + tasks + reactive).

---

## 2) Installation

### Maven repository
```xml
<repositories>
  <repository>
    <id>papermc</id>
    <url>https://repo.papermc.io/repository/maven-public/</url>
  </repository>
</repositories>
```

### Dependencies
Use API in your plugin code; use Paper module only if you need Paper integration/runtime from BSLib plugin package.

```xml
<dependencies>
  <!-- BSLib API -->
  <dependency>
    <groupId>io.github.fragmer2</groupId>
    <artifactId>bslib-api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>

  <!-- Paper API -->
  <dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>1.21.1-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

### API vs Paper module
- **`bslib-api`**: contracts and core framework API used by plugins.
- **`bslib-paper`**: Paper runtime/plugin-side integration implementation.

---

## 3) Creating Your First Plugin Using BSLib

### Step 1 — Main class
```java
import io.github.fragmer2.bslib.api.FrameworkPlugin;
import io.github.fragmer2.bslib.api.service.Services;

public final class ShopPlugin extends FrameworkPlugin {
    @Override
    public void onFrameworkEnable() {
        getLogger().info("ShopPlugin enabled with BSLib");
        Services.provide(this, ShopService.class, new ShopService());
    }

    @Override
    public void onFrameworkDisable() {
        Services.unregisterOwner(this);
    }
}
```

### Step 2 — plugin.yml (minimal)
```yaml
name: ShopPlugin
version: 1.0.0
main: com.example.shop.ShopPlugin
api-version: '1.21'
depend: [BSLib]
```

### Step 3 — Startup behavior
In `onFrameworkEnable()` register services/modules/listeners/tasks.
In `onFrameworkDisable()` unregister owner resources and cancel long-running jobs.

---

## 4) Core Concepts

## Runtime
BSLib runtime is the active framework environment initialized by BSLib plugin.

**Lifecycle flow (text diagram)**
`Plugin load -> onFrameworkEnable -> runtime operations -> onFrameworkDisable -> cleanup`

**Best practices**
- Register all framework resources during enable.
- Teardown by owner during disable.

## Owner isolation
Owner isolation means shared systems (services, messaging, capabilities) can scope state to a specific plugin.

**Lifecycle flow**
`register(owner) -> use -> unregisterOwner(owner)`

**Best practices**
- Always prefer owner-aware overloads.
- Never leave cross-plugin registrations without owner cleanup.

## Services
Service registry for cross-component/plugin contracts.

**Usage**
```java
Services.provide(this, EconomyService.class, new EconomyServiceImpl());
EconomyService eco = Services.get(EconomyService.class);
```

## Modules
Feature units with dependencies and deterministic lifecycle.

**Usage**
```java
moduleManager.register(new EconomyModule());
moduleManager.enableAll();
```

## Reactive system
Reactive values propagate changes to subscribers and derived streams.

**Usage**
```java
Reactive<Integer> coins = Reactive.of(0);
coins.subscribeSet(v -> player.sendMessage("Coins: " + v));
coins.increment();
```

## Task system
Scheduler wrapper with tracked task handles.

**Usage**
```java
FrameworkTask task = Tasks.sync().repeat(20).runTracked(this::tickShop);
```

## Messaging
Local topic-based pub/sub between plugins.

**Usage**
```java
PluginMessageBus.subscribe(this, "shop/reload", (String msg) -> reloadShop());
PluginMessageBus.publish("shop/reload", "now");
```

## Capabilities
Declare what your plugin provides/requires at runtime.

**Usage**
```java
PluginCapabilityRegistry.provide(this, "economy");
PluginCapabilityRegistry.require(this, "database");
```

---

## 5) Modules

### Create a module
```java
public final class EconomyModule implements BSModule {
    @Override public String getId() { return "economy"; }
    @Override public String[] getDependencies() { return new String[] {"database"}; }

    @Override
    public void onEnable(ModuleContext context) {
        // register services/listeners
    }

    @Override
    public void onDisable() {
        // release resources
    }
}
```

### Register and run
```java
moduleManager.register(new DatabaseModule());
moduleManager.register(new EconomyModule());
moduleManager.enableAll();
```

### Dependency handling
If dependency is missing/circular, BSLib prevents unsafe enable order and logs failure.

---

## 6) Tasks API

### Key rules
- Use `runTracked(...)` in production paths.
- Keep returned `FrameworkTask` for cancellation.
- Cancel repeating tasks on disable.

### Delayed task
```java
FrameworkTask delayed = Tasks.sync().delay(40).runTracked(() -> openShop(player));
```

### Repeating task
```java
FrameworkTask repeating = Tasks.async().repeat(100).runTracked(cache::refresh);
```

### Self-cancel task
```java
Tasks.sync().repeat(20).runTracked(task -> {
    if (shouldStop()) task.cancel();
});
```

### Sync vs async
- `sync()` => main-thread Bukkit-safe operations.
- `async()` => non-Bukkit heavy work (IO/CPU); hand off back to sync for Bukkit writes.

---

## 7) Reactive API

### Reactive value
```java
Reactive<Integer> stock = Reactive.of(10);
```

### Subscribe
```java
Subscription sub = stock.subscribeSet(v -> bossBar.setTitle("Stock: " + v));
```

### Batching
```java
stock.beginBatch();
stock.set(11);
stock.set(12);
stock.endBatch();
```

### Debounce / throttle
```java
Reactive<Integer> debounced = stock.debounce(10);
Reactive<Integer> throttled = stock.throttle(5);
```

### Disposal
```java
sub.unsubscribe();
debounced.destroy();
throttled.destroy();
stock.destroy();
```

### Practical gameplay/UI example
```java
Reactive<Integer> coins = Reactive.of(0);
Reactive<String> line = coins.map(v -> "Coins: " + v).distinctUntilChanged();
Subscription hud = line.subscribeSet(text -> sidebar.updateLine("coins", text));
```

---

## 8) Services API

### Register service
```java
Services.provide(this, PlayerDataService.class, new SqlPlayerDataService(dataSource));
```

### Get service
```java
PlayerDataService service = Services.get(PlayerDataService.class);
```

### Owner isolation and collision protection
- owner-aware registration blocks accidental overwrite from another plugin owner.
- teardown by owner removes all owned entries.

### Cleanup
```java
Services.unregisterOwner(this);
```

---

## 9) Messaging System

### Subscriber
```java
Subscription sub = PluginMessageBus.subscribe(this, "economy/changed", (EconomyChanged msg) -> {
    cache.invalidate(msg.playerId());
});
```

### Publisher
```java
PluginMessageBus.publish("economy/changed", new EconomyChanged(uuid));
```

### Cleanup
```java
PluginMessageBus.unregisterOwner(this);
sub.unsubscribe();
```

---

## 10) Capability System

### Register capabilities
```java
PluginCapabilityRegistry.provide(this, "economy", "shop-api");
PluginCapabilityRegistry.require(this, "database", "permissions");
```

### Validate dependencies
```java
List<String> missing = PluginCapabilityRegistry.validateMissing(this);
if (!missing.isEmpty()) {
    throw new IllegalStateException("Missing capabilities: " + missing);
}
```

### Optional integration pattern
```java
if (PluginCapabilityRegistry.validateMissing(this).isEmpty()) {
    // enable optional path
}
```

---

## 11) Error Handling

Use `FrameworkExceptionHandler` when wrapping custom execution boundaries.

```java
try {
    runCriticalLogic();
} catch (Throwable error) {
    FrameworkExceptionHandler.handle(
        this,
        FrameworkExceptionHandler.Source.SERVICE,
        error,
        Map.of("source", "service", "operation", "runCriticalLogic")
    );
}
```

Prefer structured context map (`source`, `action`, identifiers) for debugging in production.

---

## 12) Best Practices

### Reload safety
- treat reload as full lifecycle transition.
- unregister owner resources (`Services`, `PluginMessageBus`, `PluginCapabilityRegistry`).

### Avoid memory leaks
- cancel repeating tasks.
- unsubscribe reactive subscriptions.
- `destroy()` reactive graphs tied to session/player/gui.

### Async safety
- never call Bukkit world/entity/inventory APIs from async tasks.
- do heavy work async, apply result sync.

### Lifecycle rules
- enable: register all dependencies.
- disable: reverse cleanup order.
- keep module enable/disable idempotent.

---

## 13) Complete Example Plugin

```java
package com.example.shop;

import io.github.fragmer2.bslib.api.FrameworkPlugin;
import io.github.fragmer2.bslib.api.module.BSModule;
import io.github.fragmer2.bslib.api.module.ModuleContext;
import io.github.fragmer2.bslib.api.reactive.Reactive;
import io.github.fragmer2.bslib.api.reactive.Subscription;
import io.github.fragmer2.bslib.api.service.Services;
import io.github.fragmer2.bslib.api.task.FrameworkTask;
import io.github.fragmer2.bslib.api.task.Tasks;

public final class ShopPlugin extends FrameworkPlugin {
    private FrameworkTask autosaveTask;
    private Reactive<Integer> globalStock;

    @Override
    public void onFrameworkEnable() {
        Services.provide(this, ShopService.class, new ShopService());

        globalStock = Reactive.of(100);

        getModuleManager().register(new ShopModule(globalStock));
        getModuleManager().enableAll();

        autosaveTask = Tasks.async().repeat(20 * 30).runTracked(() -> {
            Services.get(ShopService.class).saveAll();
        });
    }

    @Override
    public void onFrameworkDisable() {
        if (autosaveTask != null) autosaveTask.cancel();
        if (globalStock != null) globalStock.destroy();

        getModuleManager().disableAll();
        Services.unregisterOwner(this);
    }

    public static final class ShopModule implements BSModule {
        private final Reactive<Integer> stock;
        private Subscription stockLogger;

        public ShopModule(Reactive<Integer> stock) {
            this.stock = stock;
        }

        @Override public String getId() { return "shop"; }

        @Override
        public void onEnable(ModuleContext context) {
            stockLogger = stock.subscribeSet(v -> context.plugin().getLogger().info("Stock now: " + v));
        }

        @Override
        public void onDisable() {
            if (stockLogger != null) stockLogger.unsubscribe();
        }
    }

    public static final class ShopService {
        public void saveAll() {
            // persist data
        }
    }
}
```

---

## 14) Migration Guide (Raw Paper -> BSLib)

### Scheduler migration
- Before: raw `BukkitScheduler` ids/manual tracking.
- After: `Tasks.*().runTracked(...)` + `FrameworkTask.cancel()`.

### Service wiring migration
- Before: static singleton + manual init order.
- After: `Services.provide(owner, type, instance)` + owner teardown.

### Feature lifecycle migration
- Before: large plugin class with mixed responsibilities.
- After: `BSModule` decomposition with dependency-aware enable order.

### Reactive state migration
- Before: imperative listener update chains.
- After: `Reactive<T>` streams + derived values + explicit disposal.

### Cross-plugin messaging migration
- Before: ad-hoc shared static hooks.
- After: `PluginMessageBus` topic contract and owner unregister.

---

This guide is intentionally practical: copy sections into your plugin, then enforce strict lifecycle cleanup and thread-safe boundaries from day one.
