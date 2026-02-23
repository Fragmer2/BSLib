# Module System

## Problem solved
Large plugins need deterministic feature startup and dependency ordering.

## Solution
`ModuleManager` enables modules via dependency graph and disables in reverse order.

## Minimal example
```java
manager.register(new EconomyModule());
manager.enableAll();
```

## Production example
```java
manager.register(new DatabaseModule());
manager.register(new EconomyModule()); // depends on database
manager.enableAll();

// during shutdown
manager.disableAll();
```

## Lifecycle safety rules
- Keep module enable/disable idempotent.
- Declare dependencies explicitly.

## Threading guarantees
- Lifecycle state is synchronized and warns on async access misuse.

## Anti-patterns
- Side effects in constructors instead of `onEnable`.
- Missing dependency declaration.

## Performance notes
- Move expensive preload into async warmups when possible.
- Keep disable path fast and deterministic.
