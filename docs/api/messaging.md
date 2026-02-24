# Messaging

## Problem solved
Plugins need local event-style communication without hard compile-time coupling.

## Solution
`PluginMessageBus` supports topic-based messaging with owner-aware subscriptions.

## Minimal example
```java
Subscription sub = PluginMessageBus.subscribe(this, "shop/purchase", (PurchaseEvent event) -> {
    audit.log(event);
});

PluginMessageBus.publish("shop/purchase", new PurchaseEvent(playerId, item, cost));
```

## Production example
```java
PluginMessageBus.subscribe(this, "economy/changed", (EconomyChanged msg) -> {
    cache.invalidate(msg.playerId());
});

@Override
public void onFrameworkDisable() {
    PluginMessageBus.unregisterOwner(this);
}
```

## Lifecycle safety rules
- Always subscribe with owner plugin.
- Use owner teardown at disable.

## Threading guarantees
- Delivery runs on calling thread; design payload handlers accordingly.

## Anti-patterns
- Anonymous global subscriptions with no owner.
- Publishing mutable payload objects reused across threads.

## Performance notes
- Keep payloads small and immutable.
- Use topic naming conventions (`domain/action`) for scale.
