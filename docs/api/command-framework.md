# Command Framework

## Problem solved
Raw Bukkit command parsing leads to repetitive conversion, permissions, and error handling.

## Solution
BSLib command registry centralizes parsing and structured failure handling.

## Minimal example
```java
@CommandInfo(name = "coins", permission = "shop.coins")
public void coins(Player sender) {
    sender.sendMessage("Balance: " + economy.get(sender));
}
```

## Production example
```java
@CommandInfo(name = "pay", permission = "shop.pay")
public void pay(Player sender, @Arg("target") Player target, @Arg("amount") int amount) {
    validator.require(amount > 0, "amount must be positive");
    service.transfer(sender.getUniqueId(), target.getUniqueId(), amount);
}
```

## Lifecycle safety rules
- Register commands during enable.
- Avoid mutable static command state.

## Threading guarantees
- Command execution path is synchronous unless explicitly offloaded.

## Anti-patterns
- Blocking I/O inside command handler.
- Swallowing parse exceptions silently.

## Performance notes
- Precompute suggestions for large datasets.
- Defer database actions through async chain APIs.
