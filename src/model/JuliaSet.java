package model;

import view.Pixel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static model.Movement.Param.*;

public class JuliaSet {
    private final double power;
    private final double cx;
    private final double cy;
    private final double scale;
    private final ConcurrentMap<Location, JuliaValue> cache;
    private Movement lastMovement = null;

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
        lastMovement = movement;
        return switch (lastMovement.param()) {
            case CX -> new JuliaSet(power, lastMovement.dir().add(cx), cy, scale);
            case CY -> new JuliaSet(power, cx, lastMovement.dir().add(cy), scale);
            case POWER -> new JuliaSet(lastMovement.dir().add(power), cx, cy, scale);
        };
    }

    public Location pixelToLocation(Pixel pixel) {
        return new Location(pixel.x() * scale, pixel.y() * scale);
    }

    public Pixel locationToPixel(Location location) {
        return new Pixel((int)(location.x() / scale), (int)(location.y() / scale));
    }

    public int getValue(Location location, int iterCount) {
        JuliaValue value = cache.computeIfAbsent(location, _ -> new JuliaValue(location));
        return value.update(power, cx, cy, iterCount);
    }

}
