package io.github.fragmer2.bslib.api.boss;

/**
 * Boss phase definition by health threshold.
 * Threshold should be in range 0.0..1.0 and means "activate phase when health <= threshold".
 */
public record BossPhase(double healthThreshold, Runnable onEnter) {

    public BossPhase {
        if (healthThreshold < 0.0 || healthThreshold > 1.0) {
            throw new IllegalArgumentException("healthThreshold must be between 0.0 and 1.0");
        }
        if (onEnter == null) {
            throw new IllegalArgumentException("onEnter cannot be null");
        }
    }
}
