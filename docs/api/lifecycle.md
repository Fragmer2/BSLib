# Lifecycle Management

## Problem solved
Reloads and shutdowns can leave orphaned listeners, tasks, and service entries.

## Solution
BSLib provides structured enable/disable surfaces and teardown helpers.

## Minimal example
```java
@Override
public void onFrameworkEnable() {
    Services.provide(this, Foo.class, new Foo());
}

@Override
public void onFrameworkDisable() {
    Services.unregisterOwner(this);
}
```

## Production example
```java
@Override
public void onFrameworkDisable() {
    moduleManager.disableAll();
    Services.unregisterOwner(this);
    PluginMessageBus.unregisterOwner(this);
    PluginCapabilityRegistry.unregisterOwner(this);
    playerBindings.forEach(Reactive::destroy);
    repeatingTasks.forEach(FrameworkTask::cancel);
}
```

## Reload safety rules
- Treat reload as full lifecycle transition.
- Never keep references to classloader-bound objects after disable.

## Anti-patterns
- Static caches without disable cleanup.
- Unscoped listeners on global bus.

## Performance notes
- Keep disable order deterministic.
- Precompute teardown collections to avoid long shutdown stalls.
