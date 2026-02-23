# BSLib API — Getting Started

## Why BSLib exists
Paper plugins often reimplement the same infrastructure: lifecycle wiring, scheduling safety, state propagation, service wiring, and reload cleanup. BSLib provides these primitives as reusable framework APIs so plugin teams can focus on gameplay logic.

## Minimal example
```java
public final class ShopPlugin extends FrameworkPlugin {
    @Override
    public void onFrameworkEnable() {
        getLogger().info("Shop ready");
    }
}
```

## Production bootstrap example
```java
@AutoScan
public final class ShopPlugin extends FrameworkPlugin {
    @Override
    public void onFrameworkEnable() {
        Services.provide(this, EconomyFacade.class, new VaultEconomyFacade());
        Tasks.sync().delay(20).run(() -> getLogger().info("Warmup complete"));
    }

    @Override
    public void onFrameworkDisable() {
        Services.unregisterOwner(this);
        PluginMessageBus.unregisterOwner(this);
        PluginCapabilityRegistry.unregisterOwner(this);
    }
}
```

## Lifecycle safety rules
- Register resources with owner-aware APIs.
- Unregister owned resources during disable.
- Keep async operations isolated from Bukkit-only calls.

## Anti-patterns
- Global static mutable singletons owned by no plugin.
- Forgetting teardown on disable/reload.

## Performance notes
- Prefer lazy services for expensive systems.
- Avoid heavy work in command/menu synchronous paths.
