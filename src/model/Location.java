package model;

public record Location(double x, double y) {
    public Location normalise() {
        if (x < 0)
            return new Location(-x, -y);
        else
            return this;
    }
}
