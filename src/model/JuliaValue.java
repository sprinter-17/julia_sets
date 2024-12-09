package model;

public class JuliaValue {
    public static final int MAX_ITER = 1000000;
    private static final double ESCAPE = 1.5 * 1.5;
    private double zx;
    private double zy;
    private boolean escaped;
    private int iter;

    public JuliaValue(Location location) {
        this.zx = location.x();
        this.zy = location.y();
        this.escaped = false;
        this.iter = 0;
    }

    public int update(double cx, double cy, int iterCount) {
        int end = Math.min(MAX_ITER, iter + iterCount);
        while (!escaped && iter++ < end) {
            double sqr_dist = zx * zx + zy * zy;
            double pow_angle = 2 * Math.atan2(zy, zx);
            zx = sqr_dist * Math.cos(pow_angle) + cx;
            zy = sqr_dist * Math.sin(pow_angle) + cy;
            escaped = sqr_dist > ESCAPE;
        }
        return escaped ? iter : MAX_ITER;
    }
}
