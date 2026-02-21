package io.github.fragmer2.bslib.api.button;

import io.github.fragmer2.bslib.api.animation.Animation;
import io.github.fragmer2.bslib.api.menu.MenuView;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Button {
    private Function<MenuView, ItemStack> renderer;
    private Consumer<ClickContext> clickHandler;
    private boolean cancel = true;
    private boolean dynamic = false; // true only for Button.dynamic()
    private String playerCommand;
    private String consoleCommand;
    private boolean closeOnClick = false;
    private Animation animation;
    private Supplier<Object> watchedValue; // for reactive updates
    private Object lastWatchedValue;       // cache for change detection
    private boolean initialCheckDone = false; // skip first reactive check (just rendered in open())

    private Button() {}

    public static Button of(ItemStack item) {
        Button b = new Button();
        b.renderer = view -> item;
        return b;
    }

    public static Button dynamic(Function<MenuView, ItemStack> renderer) {
        Button b = new Button();
        b.renderer = renderer;
        b.dynamic = true;
        return b;
    }

    /**
     * Shortcut: static item + click handler in one call.
     */
    public static Button of(ItemStack item, Consumer<ClickContext> onClick) {
        Button b = new Button();
        b.renderer = view -> item;
        b.clickHandler = onClick;
        return b;
    }

    public Button click(Consumer<ClickContext> handler) {
        this.clickHandler = handler;
        return this;
    }

    public Button cancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }

    public Button command(String command) {
        this.playerCommand = command;
        return this;
    }

    public Button consoleCommand(String command) {
        this.consoleCommand = command;
        return this;
    }

    public Button closeOnClick() {
        this.closeOnClick = true;
        return this;
    }

    public Button closeOnClick(boolean close) {
        this.closeOnClick = close;
        return this;
    }

    public Button animate(Animation animation) {
        this.animation = animation;
        return this;
    }

    /**
     * Reactive binding — auto-updates the button when the watched value changes.
     *
     * Usage with Supplier:
     *   Button.dynamic(view -> ...).bind(() -> eco.balance(player));
     *
     * Usage with Reactive:
     *   Button.dynamic(view -> ...).bind(coins);
     *
     * Usage with ReactiveList:
     *   Button.dynamic(view -> ...).bind(friendsList);
     */
    public Button bind(Supplier<Object> valueSupplier) {
        this.watchedValue = valueSupplier;
        return this;
    }

    /**
     * Bind directly to a Reactive value (version-based change detection).
     */
    public Button bind(io.github.fragmer2.bslib.api.reactive.Reactive<?> reactive) {
        this.watchedValue = reactive::version;
        return this;
    }

    /**
     * Bind to a ReactiveList.
     */
    public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveList<?> list) {
        this.watchedValue = list.asBindable();
        return this;
    }

    /**
     * Bind to a ReactiveMap.
     */
    public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveMap<?, ?> map) {
        this.watchedValue = map.asBindable();
        return this;
    }

    /**
     * Check if the watched value has changed since last check.
     * Used by the GUI refresh system. Skips the first call (just rendered).
     */
    public boolean hasValueChanged() {
        if (watchedValue == null) return false;
        try {
            Object current = watchedValue.get();
            if (!initialCheckDone) {
                lastWatchedValue = current;
                initialCheckDone = true;
                return false; // skip — button was just rendered in open()
            }
            boolean changed = !java.util.Objects.equals(current, lastWatchedValue);
            lastWatchedValue = current;
            return changed;
        } catch (Exception e) {
            return false;
        }
    }

    /** Reset reactive state (called when menu reopens). */
    public void resetReactiveState() {
        initialCheckDone = false;
        lastWatchedValue = null;
    }

    /** True if this button has a reactive binding. */
    public boolean isReactive() {
        return watchedValue != null;
    }

    /** True if this button uses a dynamic renderer (not static). */
    public boolean isDynamic() {
        return dynamic;
    }

    public boolean shouldCancel() { return cancel; }

    public ItemStack render(MenuView view) {
        return renderer.apply(view);
    }

    public Animation getAnimation() { return animation; }

    public void onClick(ClickContext ctx) {
        if (clickHandler != null) {
            clickHandler.accept(ctx);
        }
        if (playerCommand != null) {
            ctx.player().performCommand(playerCommand);
        }
        if (consoleCommand != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
        }
        if (closeOnClick) {
            ctx.close();
        }
    }
}
