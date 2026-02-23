# Tasks API

## Problem solved
Raw scheduler usage often leaks repeating tasks and obscures ownership.

## Solution
BSLib `Tasks` provides fluent scheduling + framework-owned tracking handles.

## Minimal example
```java
FrameworkTask task = Tasks.sync().repeat(20).runTracked(() -> tickShop());
```

## Production example
```java
FrameworkTask refresh = Tasks.async().delay(40).repeat(100).runTracked(() -> cache.refresh());
FrameworkTask timeout = Tasks.sync().delay(200).runTracked(() -> session.expire(player));

// explicit teardown
refresh.cancel();
timeout.cancel();
```

## Overloads covered
- `run(Runnable)`
- `run(Consumer<BukkitTask>)`
- delayed / repeating / async / timer modes

## Lifecycle safety rules
- Keep handles for repeating tasks and cancel on disable.
- Use tracked APIs for diagnostics-critical systems.

## Threading guarantees
- Async builders run off main thread.
- Sync builders run on Bukkit main thread.

## Anti-patterns
- Calling Bukkit API from async task body.
- Creating fire-and-forget repeating tasks with no shutdown path.

## Performance notes
- Batch periodic work into fewer tasks where possible.
- Use doctor metrics (`trackedTaskCount`) to detect leaks.
