# Dependency Injection

## Problem solved
Manual service wiring creates brittle constructors and hidden initialization order bugs.

## Solution
BSLib `ServiceContainer` and `Services` provide explicit registration and retrieval patterns.

## Minimal example
```java
container.register(Database.class, new SqlDatabase(config));
Database db = container.get(Database.class);
```

## Production example
```java
Services.provide(this, Database.class, "main", db);
Services.provideLazy(this, Cache.class, "players", () -> new PlayerCache(db));

Services.onProvide(Database.class, readyDb -> {
    getLogger().info("Database online");
});
```

## Lifecycle safety rules
- Use owner-aware `Services.provide(plugin, ...)`.
- Use `Services.unregisterOwner(plugin)` on disable.

## Threading guarantees
- Registry maps are concurrent; ownership checks are synchronized.
- Your service implementation thread safety remains your responsibility.

## Anti-patterns
- Using deprecated non-owner `provide(...)` in shared ecosystems.
- Injecting async-unsafe Bukkit objects into async workers.

## Performance notes
- Use `provideLazy` for heavyweight initialization.
- Cache lookups by type/name keys where needed.
