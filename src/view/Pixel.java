package view;

public record Pixel(int x, int y) {
    public static final Pixel ZERO = new Pixel(0, 0);
}
