package model;

public record Location(long x, long y) {
    private static final long RESOLUTION = Long.MAX_VALUE / 10;

    public Location(double x, double y) {
        this((long) (RESOLUTION * x), (long) (RESOLUTION * y));
    }

    public double dx() {
        return (double) x / RESOLUTION;
    }

    public double dy() {
        return (double) y / RESOLUTION;
    }
}
