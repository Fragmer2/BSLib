# Services API

## Problem solved
Cross-plugin contracts need discoverable, safe runtime registration.

## Solution
`Services` registry with owner-aware keys and teardown support.

## Minimal example
```java
Services.provide(this, Economy.class, new EconomyImpl());
Economy economy = Services.get(Economy.class);
```

## Production example
```java
Services.provide(this, Economy.class, "vault", new VaultEconomy());
Services.provideLazy(this, PlayerCache.class, () -> new PlayerCache(repository));

Services.onProvide(Permissions.class, perms -> bootstrapPermissions(perms));
```

## Lifecycle safety rules
- Never overwrite keys from another plugin owner.
- Always call `Services.unregisterOwner(plugin)` on disable.

## Threading guarantees
- Concurrent-safe reads with synchronized ownership transitions.

## Memory ownership
- Owner plugin owns lifecycle of registered instances.
- Lazy suppliers should avoid capturing obsolete plugin state on reload.

## Anti-patterns
- Global key reuse across unrelated plugins.
- Non-owner deprecated API in production ecosystems.

## Performance notes
- Use named keys for multiple implementations.
- Avoid expensive lazy supplier work on hot call paths.
