package view;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.JuliaSet;

import java.util.*;
import java.util.stream.IntStream;

import static model.JuliaValue.MAX_ITER;
import static view.JuliaCanvas.*;

class DrawTask extends Task<Image> {
    private final JuliaSet set;
    private final Palette palette;
    private final int startX;
    private final int startY;
    private final int size;
    List<Long> pixels = new ArrayList<>();

    public DrawTask(JuliaSet set, Palette palette, int startX, int startY, int size) {
        this.set = set;
        this.palette = palette;
        this.startX = startX;
        this.startY = startY;
        this.size = size;
        IntStream.range(startX, startX + size)
                .forEach(x -> IntStream.range(startY, startY + size)
                        .forEach(y -> pixels.add(toPixel(x, y))));
    }

    protected Image call() {
        int iter = 0;
        while (!isCancelled() && iter < 12) {
            WritableImage image = new WritableImage(size, size);
            PixelWriter writer = image.getPixelWriter();
            for (long pixel: pixels) {
                int x = toScreenX(pixel);
                int y = toScreenY(pixel);
                Color colour = palette.getColor(set.getValue(set.pixelToLocation(x, y), 1 << iter));
                writer.setColor(x - startX, y - startY, colour);
            }
            if (!isCancelled())
                updateValue(image);
            iter++;
        }
        return null;
    }
}
