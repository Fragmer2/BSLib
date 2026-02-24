# BSLib Production Guide

> **Audience**: Teams operating Paper plugins in real production servers with BSLib.
> **Scope**: Deployment, lifecycle safety, observability, performance, and incident response.

---

## 1) Production Readiness Checklist

- Confirm plugin dependency declaration includes `depend: [BSLib]`.
- Use owner-aware registries only:
  - `Services.provide(this, ...)`
  - `PluginMessageBus.subscribe(this, ...)`
  - `PluginCapabilityRegistry.provide/require(this, ...)`
- Ensure full teardown in `onFrameworkDisable()`:
  - `moduleManager.disableAll()`
  - `Services.unregisterOwner(this)`
  - `PluginMessageBus.unregisterOwner(this)`
  - `PluginCapabilityRegistry.unregisterOwner(this)`
  - cancel tracked repeating tasks
  - unsubscribe/destroy reactive resources
- Validate required capabilities at startup and fail fast if missing.
- Verify no blocking I/O on command handlers or sync task bodies.

---

## 2) Lifecycle Contract (Enable/Disable)

### Enable phase
1. Register services and capabilities.
2. Register modules in dependency order (or with explicit dependency declarations).
3. Enable modules.
4. Start scheduled jobs (keep all `FrameworkTask` handles).
5. Open messaging subscriptions under plugin ownership.

### Disable phase
1. Stop acceptance paths (commands/events that create new work).
2. Cancel repeating or delayed outstanding tasks.
3. Disable modules in reverse dependency order.
4. Unregister owner-scoped services, messaging, and capabilities.
5. Destroy session/menu/player reactive graphs.

**Rule**: Treat reload exactly like restart; no state may survive unless intentionally externalized.

---

## 3) Threading & Safety Rules

- **Main thread only**: Bukkit world/entity/inventory mutations.
- **Async allowed**: DB/network/CPU-heavy transformations.
- Bridge async results back to sync before touching Bukkit state.
- Do not capture `Player` or classloader-bound plugin objects in long-lived async/global structures.

---

## 4) Configuration and Deployment

- Pin compatible Paper and BSLib versions in release notes.
- Keep environment-specific values externalized (YAML/env/system props).
- Use immutable defaults and validate config on startup.
- Roll out with staged environments: local → staging → canary → full production.

### Suggested deployment flow
1. Build artifact and run smoke tests.
2. Deploy to staging with representative plugins.
3. Run startup capability validation and command/menu sanity checks.
4. Canary a subset of servers.
5. Promote globally after stability window.

---

## 5) Observability

Track at minimum:
- Startup duration and module enable times.
- Active tracked task count.
- Error rate by subsystem (commands, menus, async jobs, messaging handlers).
- Teardown completeness indicators during disable.

Log conventions:
- Include plugin, module, and correlation IDs where possible.
- Log structured failure context (operation, actor, payload key, exception class).
- Never silently swallow exceptions.

---

## 6) Performance Guidance

- Avoid per-tick allocations in hot paths.
- Prefer `distinctUntilChanged`, throttle, and debounce in noisy reactive flows.
- Batch periodic work into fewer scheduler tasks.
- Cache immutable menu item templates and precomputed suggestions.
- Measure before optimizing (timings + framework diagnostics).

---

## 7) Failure Handling & Recovery

- Fail fast at startup for missing capabilities or invalid critical config.
- For runtime external failures (DB/HTTP), use bounded retries with timeout and jitter.
- Degrade gracefully (read-only mode, reduced feature set) when possible.
- Surface operator-friendly alerts for repeated faults.

### Incident checklist
1. Identify failing subsystem and first error timestamp.
2. Check thread-context misuse (async Bukkit access).
3. Verify task leak indicators and message subscription ownership.
4. Confirm disable/enable cycle performs full cleanup.
5. Roll back to last known good version if error budget exceeded.

---

## 8) Security & Multi-Plugin Isolation

- Assume shared server environment with untrusted plugin interactions.
- Use capability declarations as explicit contracts.
- Never overwrite registry keys owned by another plugin.
- Keep topic naming stable and domain-scoped (`domain/action`).
- Avoid exposing mutable shared payloads across plugin boundaries.

---

## 9) Production Template

```java
@AutoScan
public final class ProductionPlugin extends FrameworkPlugin {
    private final List<FrameworkTask> tasks = new ArrayList<>();

    @Override
    public void onFrameworkEnable() {
        validateConfigOrThrow();

        PluginCapabilityRegistry.provide(this, "shop-api");
        PluginCapabilityRegistry.require(this, "database", "permissions");
        List<String> missing = PluginCapabilityRegistry.validateMissing(this);
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing capabilities: " + missing);
        }

        Services.provide(this, ShopService.class, new ShopService());

        getModuleManager().register(new DatabaseModule());
        getModuleManager().register(new EconomyModule());
        getModuleManager().enableAll();

        tasks.add(Tasks.async().repeat(20 * 30).runTracked(this::refreshCache));
        tasks.add(Tasks.sync().delay(20).runTracked(() -> getLogger().info("Warmup complete")));
    }

    @Override
    public void onFrameworkDisable() {
        tasks.forEach(FrameworkTask::cancel);
        getModuleManager().disableAll();
        Services.unregisterOwner(this);
        PluginMessageBus.unregisterOwner(this);
        PluginCapabilityRegistry.unregisterOwner(this);
    }
}
```

---

## 10) Pre-Release Gate

Ship only when all are true:
- Startup/disable cycle is repeatable without leaks.
- Capability validation passes in target environment.
- No known async Bukkit API misuse.
- Critical commands and menus pass smoke tests.
- Error handling paths are observable and actionable.

