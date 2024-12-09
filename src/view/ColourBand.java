package view;

import javafx.scene.paint.Color;

public class ColourBand {
    private Color colour;
    private int width;

    public ColourBand(Color colour, int width) {
        this.colour = colour;
        this.width = width;
    }

    public Color getColour() {
        return colour;
    }

    public int getWidth() {
        return width;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
