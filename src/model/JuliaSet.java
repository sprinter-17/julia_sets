package model;

import view.Pixel;

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

    public JuliaSet updateC(double deltaCX, double deltaCY) {
        return new JuliaSet(power, cx + deltaCX, cy + deltaCY, scale);
    }

    public JuliaSet updatePower(double deltaPower) {
        return new JuliaSet(power + deltaPower, cx, cy, scale);
    }

    public Location pixelToLocation(Pixel pixel) {
        return new Location(pixel.x() * scale, pixel.y() * scale);
    }

    public Pixel locationToPixel(Location location) {
        return new Pixel((int)(location.dx() / scale), (int)(location.dy() / scale));
    }

    public double getValue(Location location) {
        JuliaValue value = cache.computeIfAbsent(location, _ -> new JuliaValue(location));
        return value.update(power, cx, cy);
    }

}
