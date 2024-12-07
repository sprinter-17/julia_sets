package view;

import javafx.application.Platform;
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
    private final List<Task<Map<Pixel, Integer>>> drawTasks = new ArrayList<>();
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
        for (int x = -halfWidth; x < halfWidth; x += chunkSize) {
            for (int y = -halfHeight; y < halfHeight; y += chunkSize) {
                DrawTask task = new DrawTask(canvas.getSet(), x - origin.x(), y - origin.y(), chunkSize);
                task.valueProperty().addListener((_, _, vals) -> draw(vals));
                drawTasks.add(task);
            }
        }
    }

    private void draw(Map<Pixel, Integer> values) {
        Platform.runLater(() -> {
            GraphicsContext gr = canvas.getGraphicsContext2D();
            PixelWriter writer = gr.getPixelWriter();
            values.forEach((pix, val) -> drawPixel(writer, pix, val));
        });
    }

    private void drawPixel(PixelWriter writer, Pixel pixel, int value) {
        writer.setColor(pixel.x() + halfWidth + origin.x(), pixel.y() + halfHeight + origin.y(), palette.getColor(value));
    }

    public void start() {
        drawTasks.forEach(exec::execute);
    }

    public void cancel() {
        drawTasks.forEach(Task::cancel);
    }
}
