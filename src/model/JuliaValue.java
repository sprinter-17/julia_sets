package model;

public class JuliaValue {
    public static final int MAX_ITER = 1000000;
    private static final double ESCAPE = 1.5 * 1.5;
    private Location z;
    private boolean escaped;
    private int iter;

    public JuliaValue(Location location) {
        this.z = location;
        this.escaped = false;
        this.iter = 0;
    }

    public int update(double power, double cx, double cy, int iterCount) {
        int end = Math.min(MAX_ITER, iter + iterCount);
        while (!escaped && iter++ < end) {
            double sqr_dist = Math.pow(z.sqr_dist(), power / 2);
            double pow_angle = z.angle() * power;
            z = new Location(
                    sqr_dist * Math.cos(pow_angle) + cx,
                    sqr_dist * Math.sin(pow_angle) + cy);
            escaped = z.sqr_dist() > ESCAPE;
        }
        return escaped ? iter : MAX_ITER;
    }
}
