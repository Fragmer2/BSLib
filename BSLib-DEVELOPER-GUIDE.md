# BSLib Developer Guide

> **Audience**: Paper plugin developers building real production plugins with BSLib.  
> **Purpose**: learning path + practical workflows.  
> **Use with**: `BSLib-API-DOCUMENTATION.md` (deep explanations) and `BSLib-REFERENCE.md` (quick lookup).

---

## Learning Path
1. [Getting Started](#1-getting-started)
2. [Installation](#2-installation)
3. [First Plugin Example](#3-first-plugin-example)
4. [Core Concepts](#4-core-concepts)
5. [Plugin Lifecycle](#5-plugin-lifecycle)
6. [Modules System](#6-modules-system)
7. [Commands](#7-commands)
8. [Tasks & Scheduler](#8-tasks--scheduler)
9. [Reactive System](#9-reactive-system)
10. [Services & Messaging](#10-services--messaging)
11. [Menus / UI](#11-menus--ui)
12. [Threading Model](#12-threading-model)
13. [Best Practices](#13-best-practices)
14. [Common Mistakes](#14-common-mistakes)
15. [Advanced Usage](#15-advanced-usage)
16. [Full Example Plugin](#16-full-example-plugin)
17. [API Reference](#17-api-reference)

---

## 1) Getting Started

### What BSLib solves
BSLib removes repetitive framework work from Paper plugins:
- lifecycle orchestration,
- feature modularization,
- task lifecycle tracking,
- service wiring,
- reactive state propagation,
- cross-plugin contracts.

### BSLib vs raw Paper API
- **Raw Paper**: you manually manage system boundaries.
- **BSLib**: you compose standardized framework systems and focus on gameplay/business logic.

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

### Maven dependencies (API usage)
```xml
<dependency>
  <groupId>io.github.fragmer2</groupId>
  <artifactId>bslib-api</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
```

### Maven dependencies (Paper plugin usage)
```xml
<dependency>
  <groupId>io.papermc.paper</groupId>
  <artifactId>paper-api</artifactId>
  <version>1.21.1-R0.1-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
```

### API vs Paper integration
- `bslib-api`: plugin-facing framework API.
- `bslib-paper`: Paper runtime integration implementation shipped by BSLib plugin.

---

## 3) First Plugin Example

### `plugin.yml`
```yaml
name: ShopPlugin
main: com.example.shop.ShopPlugin
version: 1.0.0
api-version: '1.21'
depend: [BSLib]
```

### Minimal plugin main class
```java
import io.github.fragmer2.bslib.api.FrameworkPlugin;
import io.github.fragmer2.bslib.api.service.Services;

public final class ShopPlugin extends FrameworkPlugin {
    @Override
    public void onFrameworkEnable() {
        Services.provide(this, ShopService.class, new ShopService());
        getLogger().info("ShopPlugin started");
    }

    @Override
    public void onFrameworkDisable() {
        Services.unregisterOwner(this);
    }

    public static final class ShopService {}
}
```

---

## 4) Core Concepts

### Runtime
Framework runtime exists between `onFrameworkEnable()` and `onFrameworkDisable()`.

### Owner isolation
Systems that share global state support owner-scoped registration and teardown.

### Services
Runtime registry for type-keyed contracts.

### Modules
Deterministic feature units with dependency-aware lifecycle.

### Tasks
Tracked scheduling API with explicit handles.

### Reactive
Observable state graph with cleanup semantics.

### Messaging
Topic-based local pub/sub between plugins.

### Capabilities
Declare provided/required plugin-level contracts.

---

## 5) Plugin Lifecycle

### Lifecycle flow
`enable -> register systems -> operate -> disable -> cleanup all owner resources`

### Required cleanup checklist
- `Services.unregisterOwner(this)`
- `PluginMessageBus.unregisterOwner(this)`
- `PluginCapabilityRegistry.unregisterOwner(this)`
- cancel repeating `FrameworkTask`s
- unsubscribe/destroy reactive resources
- disable modules

---

## 6) Modules System

### Create a module
```java
import io.github.fragmer2.bslib.api.module.BSModule;
import io.github.fragmer2.bslib.api.module.ModuleContext;

public final class EconomyModule implements BSModule {
    @Override public String getId() { return "economy"; }
    @Override public String[] getDependencies() { return new String[] {"database"}; }

    @Override
    public void onEnable(ModuleContext context) {
        context.plugin().getLogger().info("Economy enabled");
    }

    @Override
    public void onDisable() {
        // release resources
    }
}
```

### Register and enable
```java
getModuleManager().register(new DatabaseModule());
getModuleManager().register(new EconomyModule());
getModuleManager().enableAll();
```

---

## 7) Commands

### Basic command object registration
```java
public final class AdminCommands {
    @io.github.fragmer2.bslib.api.command.Command("shop.reload")
    public void reload(org.bukkit.command.CommandSender sender) {
        sender.sendMessage("Reloaded");
    }
}

@Override
public void onFrameworkEnable() {
    getCommandRegistry().register(new AdminCommands());
}
```

### Good command practice
- keep command methods short,
- move logic into services/modules,
- never block with database/network work on command thread.

---

## 8) Tasks & Scheduler

### Sync task
```java
FrameworkTask syncTask = Tasks.sync().runTracked(() -> player.sendMessage("Hello"));
```

### Async task
```java
FrameworkTask asyncTask = Tasks.async().runTracked(() -> repository.refreshCache());
```

### Delayed task
```java
FrameworkTask delayed = Tasks.sync().delay(40).runTracked(() -> openMenu(player));
```

### Repeating task
```java
FrameworkTask repeating = Tasks.async().repeat(20 * 30).runTracked(cache::refresh);
```

### Self-cancel task
```java
Tasks.sync().repeat(20).runTracked(task -> {
    if (shouldStop()) task.cancel();
});
```

### Cleanup
Store handles and cancel on disable.

---

## 9) Reactive System

### Basic reactive value
```java
Reactive<Integer> coins = Reactive.of(0);
Subscription sub = coins.subscribeSet(v -> player.sendMessage("Coins: " + v));
coins.increment();
```

### Operators
```java
Reactive<Integer> throttled = coins.throttle(5);
Reactive<Integer> debounced = coins.debounce(10);
Reactive<String> line = coins.map(v -> "Coins: " + v).distinctUntilChanged();
```

### Batching
```java
coins.beginBatch();
coins.set(10);
coins.set(11);
coins.endBatch();
```

### Lifecycle
```java
sub.unsubscribe();
throttled.destroy();
debounced.destroy();
line.destroy();
coins.destroy();
```

---

## 10) Services & Messaging

### Services
```java
Services.provide(this, PlayerDataService.class, new SqlPlayerDataService(ds));
PlayerDataService svc = Services.get(PlayerDataService.class);
```

### Messaging
```java
Subscription sub = PluginMessageBus.subscribe(this, "shop/purchase", (PurchaseEvent event) -> {
    audit(event);
});

PluginMessageBus.publish("shop/purchase", new PurchaseEvent(player.getUniqueId()));
```

### Workflow pattern
`player joins -> load data service -> update reactive state -> publish event -> save on schedule -> cleanup on quit/disable`

---

## 11) Menus / UI

If your plugin uses BSLib menu APIs, treat each open view as session-scoped:
- open on main thread,
- bind UI to reactive values,
- unsubscribe/destroy bindings when menu closes or player quits.

```java
Reactive<Integer> stock = Reactive.of(20);
Subscription ui = stock.subscribeSet(v -> menuView.update());
// on close: ui.unsubscribe(); stock.destroy();
```

---

## 12) Threading Model

### Main-thread safe
- Bukkit entity/world/inventory mutations.

### Async safe
- IO, serialization, database calls, CPU transforms.

### Unsafe patterns
- accessing Bukkit mutable world state inside async task callbacks.

### Test runtime limitation
`bslib-api` tests run without Bukkit server. Use scheduler abstraction/test adapters and avoid direct Bukkit runtime assumptions.

---

## 13) Best Practices

- Prefer owner-aware APIs.
- Keep module boundaries clean.
- Store/cancel repeating task handles.
- Destroy transient reactive graphs.
- Wrap boundary code with structured error context.
- Validate capabilities at startup.

---

## 14) Common Mistakes

- Forgetting teardown on disable.
- Running blocking logic in command handlers.
- Async Bukkit world mutation.
- Global static state without owner model.
- Long-lived reactive subscriptions tied to players.

---

## 15) Advanced Usage

### Optional integration via capabilities
```java
PluginCapabilityRegistry.provide(this, "shop-api");
PluginCapabilityRegistry.require(this, "economy");

if (!PluginCapabilityRegistry.validateMissing(this).isEmpty()) {
    getLogger().warning("Economy integration disabled");
}
```

### Error boundary
```java
try {
    criticalWork();
} catch (Throwable error) {
    FrameworkExceptionHandler.handle(this, FrameworkExceptionHandler.Source.SERVICE, error,
            java.util.Map.of("source", "service", "operation", "criticalWork"));
}
```

---

## 16) Full Example Plugin

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
    private FrameworkTask autosave;
    private Reactive<Integer> stock;

    @Override
    public void onFrameworkEnable() {
        Services.provide(this, ShopService.class, new ShopService());

        stock = Reactive.of(100);
        getModuleManager().register(new ShopModule(stock));
        getModuleManager().enableAll();

        autosave = Tasks.async().repeat(20 * 30).runTracked(() -> Services.get(ShopService.class).saveAll());
    }

    @Override
    public void onFrameworkDisable() {
        if (autosave != null) autosave.cancel();
        if (stock != null) stock.destroy();

        getModuleManager().disableAll();
        Services.unregisterOwner(this);
        io.github.fragmer2.bslib.api.messaging.PluginMessageBus.unregisterOwner(this);
        io.github.fragmer2.bslib.api.capability.PluginCapabilityRegistry.unregisterOwner(this);
    }

    public static final class ShopService {
        public void saveAll() {}
    }

    public static final class ShopModule implements BSModule {
        private final Reactive<Integer> stock;
        private Subscription subscription;

        public ShopModule(Reactive<Integer> stock) { this.stock = stock; }

        @Override public String getId() { return "shop"; }

        @Override
        public void onEnable(ModuleContext context) {
            subscription = stock.subscribeSet(v -> context.plugin().getLogger().info("Stock=" + v));
        }

        @Override
        public void onDisable() {
            if (subscription != null) subscription.unsubscribe();
        }
    }
}
```

---

## 17) API Reference

- **Deep API walkthrough**: `BSLib-API-DOCUMENTATION.md`
- **Quick lookup / signature inventory**: `BSLib-REFERENCE.md`

Use this guide for implementation flow, then switch to the reference docs for edge-case semantics and exact signatures.
