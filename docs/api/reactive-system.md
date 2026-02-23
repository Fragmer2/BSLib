# Reactive System

## Problem solved
Traditional Bukkit state updates require manual refresh and listener fan-out.

## Solution
`Reactive<T>` propagates changes to subscribers and derived streams.

## Minimal example
```java
Reactive<Integer> coins = Reactive.of(0);
coins.onChange((oldValue, now) -> player.sendMessage("Coins: " + now));
coins.increment();
```

## Production example
```java
Reactive<Integer> balance = Reactive.of(0);
Reactive<String> badge = balance
        .throttle(2)
        .map(v -> v > 1000 ? "§6VIP" : "§7Player")
        .distinctUntilChanged();

Subscription sub = badge.subscribeSet(text -> sidebar.updateLine("rank", text));

// during shutdown/view close:
sub.unsubscribe();
badge.destroy();
balance.destroy();
```

## Operators
- `combine(a,b,...)`: build derived multi-source values.
- `throttle(ticks)`: emit at most once per interval.
- `debounce(ticks)`: emit after quiet period.
- `distinctUntilChanged()`: suppress equal emissions.

## Lifecycle safety rules
- Always `destroy()` derived reactives for menu/player scopes.
- Unsubscribe listeners tied to temporary sessions.

## Threading guarantees
- Mutation and dispatch are coordinated for visibility.
- Keep Bukkit-only side effects on main thread.

## Anti-patterns
- Capturing `Player` strongly in long-lived global reactives.
- Not destroying derived graphs for transient GUI/session state.

## Performance notes
- Prefer `distinctUntilChanged` on noisy streams.
- Use debounce/throttle for expensive UI recomputation.
