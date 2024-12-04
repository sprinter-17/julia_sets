package view;

import javafx.concurrent.Task;
import model.JuliaSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DrawTask extends Task<Map<Pixel, Double>> {
    private final JuliaSet set;
    private final int startX;
    private final int endX;
    private final int startY;
    private final int endY;
    private final int step;

    public DrawTask(JuliaSet set, int startX, int startY, int endX, int endY, int step) {
        this.set = set;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.step = step;
    }

    @Override
    protected Map<Pixel, Double> call() {
        int iter = 0;
        while (!isCancelled() && iter < 100) {
            Map<Pixel, Double> results = new HashMap<>();
            for (int x = startX; x < endX; x += step) {
                for (int y = startY; y < endY; y += step) {
                    Pixel pixel = new Pixel(x, y);
                    double value = set.getValue(set.pixelToLocation(pixel));
                    results.put(pixel, value);
                }
            }
            updateValue(results);
            iter++;
        }
        return Collections.emptyMap();
    }
}
