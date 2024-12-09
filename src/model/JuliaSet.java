package model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JuliaSet {
    private final double cx;
    private final double cy;
    private final double scale;
    private final ConcurrentMap<Location, JuliaValue> cache;

    public JuliaSet(double cx, double cy, double scale) {
        this.cx = cx;
        this.cy = cy;
        this.scale = scale;
        this.cache = new ConcurrentHashMap<>();
    }

    private JuliaSet(double cx, double cy, double scale, ConcurrentMap<Location, JuliaValue> cache) {
        this.cx = cx;
        this.cy = cy;
        this.scale = scale;
        this.cache = cache;
    }

    public JuliaSet zoom(double deltaScale) {
        return new JuliaSet(cx, cy, scale * (1 + deltaScale), cache);
    }

    public JuliaSet update(Movement movement) {
        return switch (movement.param()) {
            case CX -> new JuliaSet(movement.dir().add(cx), cy, scale);
            case CY -> new JuliaSet(cx, movement.dir().add(cy), scale);
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
