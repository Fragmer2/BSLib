package io.github.fragmer2.bslib.api.cinematic;

import io.github.fragmer2.bslib.api.task.Tasks;
import io.github.fragmer2.bslib.api.task.TestSchedulerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimelineTest {

    private TestSchedulerAdapter scheduler;

    @BeforeEach
    void setup() {
        scheduler = new TestSchedulerAdapter();
        Tasks.setScheduler(scheduler);
    }


    @Test
    void rejectsInvalidSeconds() {
        Timeline timeline = Timeline.create();

        assertThrows(IllegalArgumentException.class, () -> timeline.atSeconds(-1, () -> {}));
        assertThrows(IllegalArgumentException.class, () -> timeline.atSeconds(Double.NaN, () -> {}));
        assertThrows(IllegalArgumentException.class, () -> timeline.atSeconds(Double.POSITIVE_INFINITY, () -> {}));
    }

    @Test
    void executesEntriesInOrderByTick() {
        List<String> events = new ArrayList<>();

        Timeline.create()
                .atTicks(40, () -> events.add("second"))
                .atTicks(20, () -> events.add("first"))
                .play();

        scheduler.tick(20);
        assertEquals(List.of("first"), events);

        scheduler.tick(20);
        assertEquals(List.of("first", "second"), events);
    }
}
