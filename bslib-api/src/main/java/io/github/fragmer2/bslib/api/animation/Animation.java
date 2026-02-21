package io.github.fragmer2.bslib.api.animation;

import java.util.Arrays;
import java.util.List;

public class Animation {
    private final List<Frame> frames;
    private final boolean loop;

    private Animation(List<Frame> frames, boolean loop) {
        this.frames = frames;
        this.loop = loop;
    }

    public static Animation of(List<Frame> frames, boolean loop) {
        return new Animation(frames, loop);
    }

    public static Animation of(Frame... frames) {
        return new Animation(Arrays.asList(frames), true);
    }

    public static Animation once(Frame... frames) {
        return new Animation(Arrays.asList(frames), false);
    }

    public List<Frame> getFrames() { return frames; }
    public boolean isLoop() { return loop; }
}