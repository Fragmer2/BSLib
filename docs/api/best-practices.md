# Best Practices

## Production checklist
- Use owner-aware registry APIs (`Services`, `PluginMessageBus`, `PluginCapabilityRegistry`).
- Cancel repeating tasks and destroy reactive graphs on disable.
- Keep module dependencies explicit and deterministic.
- Route failures through framework exception handling paths.

## Threading model
- Main-thread only: Bukkit entity/world/inventory APIs.
- Async allowed: database, HTTP, CPU transforms.
- Bridge async → sync for game-state writes.

## Reload safety
- Consider reload equivalent to full restart for your plugin resources.
- Never keep stale class references in static fields.

## Memory ownership
- Player/session-bound objects must have explicit close/destroy points.
- Do not capture `Player` in global singletons.

## Anti-patterns
- Fire-and-forget async without timeout/error handling.
- Silent exception swallowing.
- Global mutable maps without ownership boundaries.

## Performance notes
- Avoid per-tick allocations in hot loops.
- Coalesce menu refreshes/reactive emissions.
- Measure with doctor metrics and timings before optimizing.
