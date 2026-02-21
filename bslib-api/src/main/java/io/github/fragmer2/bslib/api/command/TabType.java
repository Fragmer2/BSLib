package io.github.fragmer2.bslib.api.command;

/**
 * Built-in tab completion types for common argument types.
 */
public enum TabType {
    /** No auto-completion. */
    NONE,
    /** Online player names. */
    ONLINE_PLAYERS,
    /** All material names. */
    MATERIALS,
    /** World names. */
    WORLDS,
    /** Number suggestions (1-64). */
    NUMBERS,
    /** Boolean (true/false). */
    BOOLEAN
}
