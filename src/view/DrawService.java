package view;

import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DrawService {
    private final List<Task<Map<Pixel, Double>>> drawTasks = new ArrayList<>();
    private final JuliaCanvas canvas;
    private final Pixel origin;
    private final Palette palette;
    private final int halfWidth;
    private final int halfHeight;


    private final Executor exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    public DrawService(JuliaCanvas canvas, Pixel origin, int chunkSize, Palette palette) {
        this.canvas = canvas;
        this.origin = origin;
        this.halfWidth = (int) canvas.getWidth() / 2;
        this.halfHeight = (int) canvas.getHeight() / 2;
        this.palette = palette;
        for (int x = 0; x < chunkSize; x += 1) {
            for (int y = 0; y < chunkSize; y += 1) {
                DrawTask task = new DrawTask(canvas.getSet(), x - halfWidth - origin.x(), y - halfHeight - origin.y(), x + halfWidth - origin.x(), y + halfHeight - origin.y(), chunkSize);
                task.valueProperty().addListener((_, _, vals) -> draw(vals));
                drawTasks.add(task);
            }
        }
        Collections.shuffle(drawTasks);
    }

    private void draw(Map<Pixel,Double> values) {
        GraphicsContext gr = canvas.getGraphicsContext2D();
        PixelWriter writer = gr.getPixelWriter();
        values.forEach((pix, val) -> drawPixel(writer, pix, val));
    }

    private void drawPixel(PixelWriter writer, Pixel pixel, double value) {
        writer.setColor(pixel.x() + halfWidth + origin.x(), pixel.y() + halfHeight + origin.y(), palette.getColor(value));
    }

    public void start() {
        drawTasks.forEach(exec::execute);
    }

    public void cancel() {
        drawTasks.forEach(Task::cancel);
    }
}
