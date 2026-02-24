# Capabilities

## Problem solved
Cross-plugin dependencies need runtime declaration beyond hard plugin.yml dependencies.

## Solution
`PluginCapabilityRegistry` allows declaring provided/required capabilities.

## Minimal example
```java
PluginCapabilityRegistry.provide(this, "economy");
PluginCapabilityRegistry.require(this, "database");
```

## Production example
```java
PluginCapabilityRegistry.provide(this, "shop-api", "economy");
PluginCapabilityRegistry.require(this, "database", "permissions");

List<String> missing = PluginCapabilityRegistry.validateMissing(this);
if (!missing.isEmpty()) {
    throw new IllegalStateException("Missing capabilities: " + missing);
}
```

## Lifecycle safety rules
- Owner claims prevent capability hijacking.
- Unregister owner capabilities during disable.

## Anti-patterns
- Using plugin-name-only ownership assumptions in shared deployments.
- Ignoring missing requirements and failing later at runtime.

## Performance notes
- Validate once at startup and cache result.
- Keep capability vocabulary stable and documented.
