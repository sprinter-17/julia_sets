package view;

import javafx.concurrent.Task;
import model.JuliaSet;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DrawTask extends Task<Map<Pixel, Integer>> {
    private final JuliaSet set;
    private final int startX;
    private final int startY;
    private final int size;
    List<Pixel> pixels = new ArrayList<>();

    public DrawTask(JuliaSet set, int startX, int startY, int size) {
        this.set = set;
        this.startX = startX;
        this.startY = startY;
        this.size = size;
        IntStream.range(startX, startX + size)
                .forEach(x -> IntStream.range(startY, startY + size)
                        .forEach(y -> pixels.add(new Pixel(x, y))));
    }

    private Function<Pixel,Integer> evaluator(int iter) {
        return pix -> set.getValue(set.pixelToLocation(pix), 1 << iter);
    }

    protected Map<Pixel, Integer> call() {
        int iter = 0;
        while (!isCancelled() && iter < 8) {
            Function<Pixel, Integer> evalutator = evaluator(iter);
            Map<Pixel, Integer> results = pixels.stream().parallel()
                    .collect(Collectors.toMap(pix -> pix, evalutator));
            if (!isCancelled())
                updateValue(results);
            iter++;
        }
        return Collections.emptyMap();
    }
}
