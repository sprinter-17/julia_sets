package view;

import javafx.concurrent.Task;
import model.JuliaSet;

import java.util.*;
import java.util.stream.IntStream;

import static model.JuliaValue.MAX_ITER;
import static view.JuliaCanvas.*;

class DrawTask extends Task<Map<Long, Integer>> {
    private final JuliaSet set;
    List<Long> pixels = new ArrayList<>();

    public DrawTask(JuliaSet set, int startX, int startY, int size) {
        this.set = set;
        IntStream.range(startX, startX + size)
                .forEach(x -> IntStream.range(startY, startY + size)
                        .forEach(y -> pixels.add(toPixel(x, y))));
    }

    protected Map<Long, Integer> call() {
        int iter = 0;
        while (!isCancelled() && iter < 12) {
            Map<Long,Integer> results = new HashMap<>();
            for (long pixel: pixels) {
                int x = toScreenX(pixel);
                int y = toScreenY(pixel);
                results.put(pixel, set.getValue(set.pixelToLocation(x, y), 1 << iter));
            }
            if (!isCancelled())
                updateValue(results);
            if (results.values().stream().allMatch(v -> v < MAX_ITER))
                return results;
            iter++;
        }
        return Collections.emptyMap();
    }
}
