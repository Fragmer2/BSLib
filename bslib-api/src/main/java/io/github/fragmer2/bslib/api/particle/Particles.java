package io.github.fragmer2.bslib.api.particle;

import org.bukkit.Location;
import org.bukkit.Particle;

public final class Particles {
    private Particles() {}

    public static ParticlePattern circle(Location center, double radius) {
        return ParticlePattern.circle(center, radius);
    }

    public static ParticlePattern spiral(Location center, double radius, double height) {
        return ParticlePattern.spiral(center, radius, height);
    }

    public static ParticlePattern beam(Location from, Location to) {
        return ParticlePattern.beam(from, to);
    }

    public static ParticlePattern single(Location location) {
        return ParticlePattern.single(location).particle(Particle.END_ROD);
    }
}
