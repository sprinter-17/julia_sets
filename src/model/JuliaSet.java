package model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JuliaSet {
    private final double power;
    private final double cx;
    private final double cy;
    private final double scale;
    private final ConcurrentMap<Location, JuliaValue> cache;

    public JuliaSet(double power, double cx, double cy, double scale) {
        this.power = power;
        this.cx = cx;
        this.cy = cy;
        this.scale = scale;
        this.cache = new ConcurrentHashMap<>();
    }

    private JuliaSet(double power, double cx, double cy, double scale, ConcurrentMap<Location, JuliaValue> cache) {
        this.power = power;
        this.cx = cx;
        this.cy = cy;
        this.scale = scale;
        this.cache = cache;
        System.out.println(scale);
    }

    public JuliaSet zoom(double deltaScale) {
        return new JuliaSet(power, cx, cy, scale * (1 + deltaScale), cache);
    }

    public JuliaSet update(Movement movement) {
        return switch (movement.param()) {
            case CX -> new JuliaSet(power, movement.dir().add(cx), cy, scale);
            case CY -> new JuliaSet(power, cx, movement.dir().add(cy), scale);
            case POWER -> new JuliaSet(movement.dir().add(power), cx, cy, scale);
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
        return value.update(power, cx, cy, iterCount);
    }

}
