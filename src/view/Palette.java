package view;

import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Deque;

public class Palette {
    private final int size;
    private final Color[] colours;

    private record Band(Color colour, int width) {
    }

    public Palette(int size) {
        this.size = size;
        this.colours = new Color[size];

        Deque<Band> bands = new ArrayDeque<>();
        bands.add(new Band(Color.GOLD, 20));
        bands.add(new Band(Color.SALMON, 40));
        bands.add(new Band(Color.GREENYELLOW, 280));
        bands.add(new Band(Color.GOLDENROD, 450));
        bands.add(new Band(Color.ALICEBLUE, 600));
        bands.add(new Band(Color.OLIVEDRAB, 6000));

        Color colour = Color.WHITE;
        double distance = 0.0;

        for (int value = 0; value < size; value ++) {
            if (distance > 1.0) {
                colour = bands.removeFirst().colour();
                distance = 0.0;
            }
            int index = getIndex(value);
            if (bands.isEmpty()) {
                colours[index] = colour.interpolate(Color.BLACK, 1.0 * value / size);
            } else {
                colours[index] = colour.interpolate(bands.getFirst().colour(), distance);
                distance += 1.0 / bands.getFirst().width();
            }
        }
    }

    private int getIndex(int value) {
        return Math.max(0, Math.min(size - 1, value));
    }

    public Color getColor(int value) {
        return  colours[getIndex(value)];
    }
}
