package io.github.fragmer2.bslib.api.menu;

import io.github.fragmer2.bslib.api.GuiManagerProvider;
import io.github.fragmer2.bslib.api.button.Button;
import io.github.fragmer2.bslib.api.item.Item;
import io.github.fragmer2.bslib.api.reactive.Reactive;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Declarative React-style GUI.
 *
 * Instead of imperative setup():
 *   setButton(13, Button.dynamic(view -> ...));
 *
 * You write a render function:
 *   ReactiveMenu menu = ReactiveMenu.create("Shop", 3)
 *       .render(ctx -> {
 *           ctx.button(13)
 *              .item(Material.DIAMOND)
 *              .text("Coins: " + coins.get())
 *              .onClick(e -> coins.add(10));
 *
 *           ctx.fill(Material.GRAY_STAINED_GLASS_PANE);
 *       })
 *       .bind(coins)  // re-render when coins change
 *       .build();
 *
 *   menu.open(player);
 *
 * ===== Full example: =====
 *   Reactive<Integer> page = Reactive.of(0);
 *   List<ItemStack> items = getShopItems();
 *
 *   ReactiveMenu.create("Shop", 6)
 *       .render(ctx -> {
 *           int offset = page.get() * 45;
 *           for (int i = 0; i < 45 && offset + i < items.size(); i++) {
 *               int idx = offset + i;
 *               ctx.button(i)
 *                  .item(items.get(idx))
 *                  .onClick(e -> buyItem(e.player(), idx));
 *           }
 *           ctx.button(45).item(Material.ARROW).text("← Previous")
 *              .onClick(e -> { if (page.get() > 0) page.decrement(); });
 *           ctx.button(53).item(Material.ARROW).text("Next →")
 *              .onClick(e -> page.increment());
 *       })
 *       .bind(page)
 *       .build()
 *       .open(player);
 *
 * The render function is re-called every time a bound Reactive changes.
 * GUI auto-updates. No manual update code needed.
 */
public class ReactiveMenu {
    private final String title;
    private final int rows;
    private Consumer<RenderContext> renderFn;
    private final List<Reactive<?>> bindings = new ArrayList<>();
    private final Set<InventoryPolicy> policies = EnumSet.noneOf(InventoryPolicy.class);

    private ReactiveMenu(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }

    // ========== Builder ==========

    public static ReactiveMenu create(String title, int rows) {
        return new ReactiveMenu(title, rows);
    }

    /**
     * Set the render function. Called on open and on every state change.
     */
    public ReactiveMenu render(Consumer<RenderContext> renderFn) {
        this.renderFn = renderFn;
        return this;
    }

    /**
     * Bind to a Reactive — re-renders when it changes.
     */
    public ReactiveMenu bind(Reactive<?>... reactives) {
        bindings.addAll(Arrays.asList(reactives));
        return this;
    }

    public ReactiveMenu secure() {
        policies.addAll(EnumSet.allOf(InventoryPolicy.class));
        return this;
    }

    public ReactiveMenu policy(InventoryPolicy... policies) {
        this.policies.addAll(Arrays.asList(policies));
        return this;
    }

    /**
     * Build the menu (returns a Menu that can be opened).
     */
    public Menu build() {
        return new DeclarativeMenu(title, rows, renderFn, bindings, policies);
    }

    /**
     * Build and immediately open for a player.
     */
    public void open(Player player) {
        build().open(player);
    }

    // ========== Internal Menu implementation ==========

    private static class DeclarativeMenu extends Menu {
        private final Consumer<RenderContext> renderFn;
        private final List<Reactive<?>> bindings;
        private final long[] lastVersions;

        DeclarativeMenu(String title, int rows, Consumer<RenderContext> renderFn,
                        List<Reactive<?>> bindings, Set<InventoryPolicy> policies) {
            super(title, rows);
            this.renderFn = renderFn;
            this.bindings = bindings;
            this.lastVersions = new long[bindings.size()];
            for (InventoryPolicy p : policies) policy(p);
        }

        @Override
        protected void setup() {
            rerender();
        }

        /**
         * Called by the refresh system — check if any binding changed.
         * If so, re-run the render function and update buttons.
         */
        boolean checkAndRerender() {
            boolean changed = false;
            for (int i = 0; i < bindings.size(); i++) {
                long current = bindings.get(i).version();
                if (current != lastVersions[i]) {
                    lastVersions[i] = current;
                    changed = true;
                }
            }
            if (changed) {
                rerender();
            }
            return changed;
        }

        private void rerender() {
            getButtons().clear();
            RenderContext ctx = new RenderContext(this);
            if (renderFn != null) {
                renderFn.accept(ctx);
            }
        }

        boolean hasBindings() {
            return !bindings.isEmpty();
        }
    }

    // ========== Render Context (DSL) ==========

    /**
     * Context passed to the render function.
     * Provides a fluent DSL for declaring buttons.
     */
    public static class RenderContext {
        private final Menu menu;

        RenderContext(Menu menu) {
            this.menu = menu;
        }

        /**
         * Declare a button at a slot.
         *   ctx.button(13).item(Material.DIAMOND).text("Buy").onClick(e -> buy());
         */
        public ButtonBuilder button(int slot) {
            return new ButtonBuilder(menu, slot);
        }

        /**
         * Fill all empty slots with a material.
         */
        public void fill(Material material) {
            fill(new ItemStack(material));
        }

        /**
         * Fill all empty slots with an item.
         */
        public void fill(ItemStack filler) {
            int total = menu.getRows() * 9;
            for (int i = 0; i < total; i++) {
                if (!menu.getButtons().containsKey(i)) {
                    menu.getButtons().put(i, Button.of(filler));
                }
            }
        }

        /**
         * Set a border around the edges.
         */
        public void border(Material material) {
            border(new ItemStack(material));
        }

        public void border(ItemStack item) {
            int cols = 9;
            int rows = menu.getRows();
            for (int i = 0; i < cols; i++) {
                menu.getButtons().putIfAbsent(i, Button.of(item));                    // top
                menu.getButtons().putIfAbsent((rows - 1) * cols + i, Button.of(item)); // bottom
            }
            for (int r = 1; r < rows - 1; r++) {
                menu.getButtons().putIfAbsent(r * cols, Button.of(item));          // left
                menu.getButtons().putIfAbsent(r * cols + cols - 1, Button.of(item)); // right
            }
        }

        /**
         * Set a row of buttons.
         */
        public void row(int row, Button button) {
            for (int col = 0; col < 9; col++) {
                menu.getButtons().put(row * 9 + col, button);
            }
        }

        /**
         * Access the underlying menu for advanced operations.
         */
        public Menu menu() { return menu; }
    }

    // ========== Button Builder (DSL) ==========

    /**
     * Fluent builder for declaring a button in the render context.
     */
    public static class ButtonBuilder {
        private final Menu menu;
        private final int slot;
        private ItemStack item;
        private Material material;
        private String text;
        private List<String> lore;
        private Consumer<io.github.fragmer2.bslib.api.button.ClickContext> onClick;
        private boolean closeOnClick = false;
        private String command;

        ButtonBuilder(Menu menu, int slot) {
            this.menu = menu;
            this.slot = slot;
        }

        /** Set item directly. */
        public ButtonBuilder item(ItemStack item) {
            this.item = item;
            return done();
        }

        /** Set material (creates a simple item). */
        public ButtonBuilder item(Material material) {
            this.material = material;
            return this;
        }

        /** Set display name (MiniMessage format). */
        public ButtonBuilder text(String text) {
            this.text = text;
            return this;
        }

        /** Set lore lines. */
        public ButtonBuilder lore(String... lines) {
            this.lore = Arrays.asList(lines);
            return this;
        }

        /** Set click handler. */
        public ButtonBuilder onClick(Consumer<io.github.fragmer2.bslib.api.button.ClickContext> handler) {
            this.onClick = handler;
            return done();
        }

        /** Close menu on click. */
        public ButtonBuilder closeOnClick() {
            this.closeOnClick = true;
            return done();
        }

        /** Run a player command on click. */
        public ButtonBuilder command(String command) {
            this.command = command;
            return done();
        }

        /**
         * Finalize and register the button.
         * Called automatically when onClick/item(ItemStack)/closeOnClick/command is set,
         * or call manually: ctx.button(0).item(Material.STONE).text("Hi").done();
         */
        public ButtonBuilder done() {
            // Build the item
            ItemStack finalItem = item;
            if (finalItem == null && material != null) {
                Item builder = Item.of(material);
                if (text != null) builder.name(text);
                if (lore != null) builder.lore(lore.toArray(new String[0]));
                finalItem = builder.build();
            }
            if (finalItem == null) finalItem = new ItemStack(Material.BARRIER);

            // Build the button
            Button button = Button.of(finalItem);
            if (onClick != null) button = button.click(onClick);
            if (closeOnClick) button = button.closeOnClick();
            if (command != null) button = button.command(command);

            menu.getButtons().put(slot, button);
            return this;
        }
    }

    // ========== Integration with GuiManager ==========

    /**
     * Check if a Menu is a DeclarativeMenu with bindings (for refresh system).
     */
    public static boolean isDeclarative(Menu menu) {
        return menu instanceof DeclarativeMenu dm && dm.hasBindings();
    }

    /**
     * Check and re-render a DeclarativeMenu if bindings changed.
     * Returns true if re-rendered.
     */
    public static boolean checkRerender(Menu menu) {
        if (menu instanceof DeclarativeMenu dm) {
            return dm.checkAndRerender();
        }
        return false;
    }
}
