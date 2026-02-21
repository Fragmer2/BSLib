package io.github.fragmer2.bslib.api.menu;

import io.github.fragmer2.bslib.api.button.Button;
import java.util.HashMap;
import java.util.Map;

public abstract class LayoutMenu extends Menu {

    private final Map<Character, Button> buttonMap = new HashMap<>();
    private final String[] layout;

    public LayoutMenu(String title, int rows, String... layout) {
        super(title, rows);
        if (layout.length != rows) {
            throw new IllegalArgumentException("Layout must have exactly " + rows + " lines");
        }
        this.layout = layout;
        setupLayout();          // пользователь заполняет buttonMap
        applyLayout();          // расставляем кнопки по layout
    }

    @Override
    protected final void setup() {
        // ничего не делаем – инициализация уже выполнена в конструкторе
    }

    protected abstract void setupLayout();

    protected void bind(char symbol, Button button) {
        buttonMap.put(symbol, button);
    }

    private void applyLayout() {
        for (int row = 0; row < layout.length; row++) {
            String line = layout[row];
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                if (symbol != ' ') {
                    Button btn = buttonMap.get(symbol);
                    if (btn != null) {
                        int slot = row * 9 + col;
                        setButton(slot, btn);
                    }
                }
            }
        }
    }
}