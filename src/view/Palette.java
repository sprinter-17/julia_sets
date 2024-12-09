package view;

import javafx.scene.paint.Color;

import java.util.List;

import static model.JuliaValue.MAX_ITER;

public class Palette {
    private final int size;
    private final Color[] colours;

    public Palette(List<ColourBand> bands) {
        this.size = MAX_ITER;
        this.colours = new Color[size];

        Color colour = Color.WHITE;
        double distance = 0.0;

        int currentBand = 0;
        for (int value = 0; value < size; value ++) {
            if (distance > 1.0) {
                colour = bands.get(currentBand).getColour();
                currentBand++;
                distance = 0.0;
            }
            int index = getIndex(value);
            if (currentBand == bands.size()) {
                colours[index] = colour.interpolate(Color.BLACK, 1.0 * value / size);
            } else {
                colours[index] = colour.interpolate(bands.get(currentBand).getColour(), distance);
                distance += 1.0 / bands.get(currentBand).getWidth();
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
