package view;

import javafx.scene.paint.Color;

public class Palette {
    private final int size;
    private final Color[] colours;

    public Palette(int size) {
        this.size = size;
        this.colours = new Color[size];

        for (double val = 0; val < 1.0; val += 1.0 / size) {
            double red = 0.2 - val / 5;
            double green = 0.5 - val / 2;
            double blue = 1.0 - val;
            colours[getIndex(val)] = new Color(red, green, blue, 1.0);
        }
    }

    private int getIndex(double value) {
        return Math.max(0, Math.min(size - 1, (int)(value * size)));
    }



    public Color getColor(double value) {
        return  colours[getIndex(value)];
    }
}
