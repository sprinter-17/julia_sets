package model;

public record Location(double x, double y) {
    public double sqr_dist() {
        return x * x + y * y;
    }

    public double angle() {
        return Math.atan2(y, x);
    }
}
