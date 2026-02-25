package io.github.fragmer2.bslib.api.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Declarative particle shape with chainable options.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Particle Engine preview")
public final class ParticlePattern {
    private final List<Location> points;
    private Particle particle = Particle.DUST;
    private int count = 1;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double extra;
    private Color color = Color.RED;
    private float size = 1.0f;

    private ParticlePattern(List<Location> points) {
        this.points = points;
    }

    public static ParticlePattern circle(Location center, double radius) {
        Objects.requireNonNull(center, "center");
        List<Location> points = new ArrayList<>();
        for (int angle = 0; angle < 360; angle += 10) {
            double rad = Math.toRadians(angle);
            points.add(center.clone().add(Math.cos(rad) * radius, 0.0, Math.sin(rad) * radius));
        }
        return new ParticlePattern(points);
    }

    public static ParticlePattern spiral(Location center, double radius, double height) {
        Objects.requireNonNull(center, "center");
        List<Location> points = new ArrayList<>();
        for (double t = 0; t < Math.PI * 4; t += Math.PI / 12) {
            double x = Math.cos(t) * radius;
            double z = Math.sin(t) * radius;
            double y = (t / (Math.PI * 4)) * height;
            points.add(center.clone().add(x, y, z));
        }
        return new ParticlePattern(points);
    }

    public static ParticlePattern beam(Location from, Location to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (from.getWorld() == null || to.getWorld() == null || !from.getWorld().equals(to.getWorld())) {
            throw new IllegalArgumentException("from and to must be in the same world");
        }

        List<Location> points = new ArrayList<>();
        double distance = from.distance(to);
        if (distance == 0) {
            points.add(from.clone());
            return new ParticlePattern(points);
        }

        org.bukkit.util.Vector direction = to.toVector().subtract(from.toVector()).normalize();
        for (double step = 0; step <= distance; step += 0.4) {
            points.add(from.clone().add(direction.clone().multiply(step)));
        }
        return new ParticlePattern(points);
    }

    public static ParticlePattern single(Location location) {
        return new ParticlePattern(new ArrayList<>(List.of(Objects.requireNonNull(location, "location").clone())));
    }

    public ParticlePattern particle(Particle particle) {
        this.particle = Objects.requireNonNull(particle, "particle");
        return this;
    }

    public ParticlePattern count(int count) {
        this.count = Math.max(1, count);
        return this;
    }

    public ParticlePattern offset(double x, double y, double z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public ParticlePattern speed(double extra) {
        this.extra = extra;
        return this;
    }

    public ParticlePattern color(Color color) {
        this.color = Objects.requireNonNull(color, "color");
        return this;
    }

    public ParticlePattern size(float size) {
        if (size <= 0.0f) {
            throw new IllegalArgumentException("size must be > 0");
        }
        this.size = size;
        return this;
    }

    public void play() {
        for (Location point : points) {
            World world = point.getWorld();
            if (world == null) {
                continue;
            }

            if (particle == Particle.DUST) {
                world.spawnParticle(particle, point, count, offsetX, offsetY, offsetZ, extra,
                        new Particle.DustOptions(color, size));
            } else {
                world.spawnParticle(particle, point, count, offsetX, offsetY, offsetZ, extra);
            }
        }
    }

    public void play(Player player) {
        Objects.requireNonNull(player, "player");
        for (Location point : points) {
            if (particle == Particle.DUST) {
                player.spawnParticle(particle, point, count, offsetX, offsetY, offsetZ, extra,
                        new Particle.DustOptions(color, size));
            } else {
                player.spawnParticle(particle, point, count, offsetX, offsetY, offsetZ, extra);
            }
        }
    }
}
