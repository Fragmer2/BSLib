# GUI Framework

## Problem solved
Inventory GUI code is error-prone: click routing, render diffs, and teardown leaks.

## Solution
BSLib menu APIs structure view lifecycle and refresh behavior.

## Minimal example
```java
Menu menu = Menus.chest("Shop", 3)
        .button(13, Button.item(Material.EMERALD, "Buy").onClick(ctx -> buy(ctx.player())))
        .build();

gui.openMenu(player, menu);
```

## Production example
```java
Reactive<Integer> stock = Reactive.of(20);
MenuView view = gui.openMenu(player, createShopMenu(stock));

Subscription stockSub = stock.subscribeSet(value -> view.update());

// on close
stockSub.unsubscribe();
stock.destroy();
```

## Lifecycle safety rules
- Pair every open view with close cleanup.
- Route menu exceptions through framework handler (default in BSLib).

## Threading guarantees
- Inventory interactions must remain sync/main-thread.

## Anti-patterns
- Async direct inventory mutation.
- Holding `MenuView` references after player quit.

## Performance notes
- Favor dynamic slot refresh, not full rerender every tick.
- Cache item templates for hot menus.
