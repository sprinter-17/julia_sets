package model;

class JuliaValue {
    private static final int MAX_ITER = 100000;
    private static final double ESCAPE = 1.5 * 1.5;
    private double zx;
    private double zy;
    private boolean escaped;
    private int iter;

    public JuliaValue(Location location) {
        this.zx = location.dx();
        this.zy = location.dy();
        this.escaped = false;
        this.iter = 0;
    }

    public double update(double power, double cx, double cy) {
        int end = Math.min(MAX_ITER, iter + 50);
        while (!escaped && iter < end) {
            double sqr = Math.pow(zx * zx + zy * zy, power / 2);
            double angle = Math.atan2(zy, zx);
            zx = sqr * Math.cos(power * angle) + cx;
            zy = sqr * Math.sin(power * angle) + cy;
            escaped = zx * zx + zy * zy > ESCAPE;
            iter++;
        }
        return escaped ? (double)iter / MAX_ITER : 1.0;
    }
}
