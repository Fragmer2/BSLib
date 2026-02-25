package io.github.fragmer2.bslib.api.cinematic;

import io.github.fragmer2.bslib.api.task.Tasks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Simple cutscene timeline based on server ticks.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Cinematic API preview")
public final class Timeline {
    private final List<Entry> entries = new ArrayList<>();

    private Timeline() {}

    public static Timeline create() {
        return new Timeline();
    }

    public Timeline atTicks(long ticks, Runnable action) {
        if (ticks < 0) {
            throw new IllegalArgumentException("ticks must be >= 0");
        }
        entries.add(new Entry(ticks, Objects.requireNonNull(action, "action")));
        return this;
    }

    public Timeline atSeconds(double seconds, Runnable action) {
        if (!Double.isFinite(seconds) || seconds < 0) {
            throw new IllegalArgumentException("seconds must be finite and >= 0");
        }
        return atTicks(Math.round(seconds * 20.0), action);
    }

    public void play() {
        entries.stream()
                .sorted(Comparator.comparingLong(Entry::ticks))
                .forEach(entry -> Tasks.sync().delay(entry.ticks()).runTracked(entry.action()));
    }

    private record Entry(long ticks, Runnable action) {}
}
