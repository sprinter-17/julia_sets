package model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JuliaSet {
    private final double cx;
    private final double cy;
    private final double scale;
    private final ConcurrentMap<Location, JuliaValue> cache;
    private double delta;

    public JuliaSet(double cx, double cy, double scale, double delta) {
        this(cx, cy, scale, delta, new ConcurrentHashMap<>());
    }

    private JuliaSet(double cx, double cy, double scale, double delta, ConcurrentMap<Location, JuliaValue> cache) {
        this.cx = cx;
        this.cy = cy;
        this.scale = scale;
        this.cache = cache;
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public JuliaSet zoom(double deltaScale) {
        return new JuliaSet(cx, cy, scale * (1 + deltaScale), delta, cache);
    }

    public JuliaSet update(Movement movement) {
        return switch (movement.param()) {
            case CX -> new JuliaSet(movement.dir().apply(delta) + cx, cy, scale, delta);
            case CY -> new JuliaSet(cx, movement.dir().apply(delta) + cy, scale, delta);
        };
    }

    public Location pixelToLocation(int x, int y) {
        return new Location(x * scale, y * scale);
    }

    public int pixelX(Location location) {
        return (int)(location.x() / scale);
    }

    public int pixelY(Location location) {
        return (int)(location.y() / scale);
    }

    public int getValue(Location location, int iterCount) {
        JuliaValue value = cache.computeIfAbsent(location.normalise(), _ -> new JuliaValue(location));
        return value.update(cx, cy, iterCount);
    }

}
