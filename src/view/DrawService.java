package view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static view.JuliaCanvas.toScreenX;
import static view.JuliaCanvas.toScreenY;

class DrawService {
    private final List<Task<Map<Long, Integer>>> drawTasks = new ArrayList<>();
    private final JuliaCanvas canvas;
    private final int originX;
    private final int originY;
    private final Palette palette;
    private final int halfWidth;
    private final int halfHeight;
    private boolean cancelled = false;


    private final Executor exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    public DrawService(JuliaCanvas canvas, int originX, int originY, int chunkSize, Palette palette) {
        this.canvas = canvas;
        this.originX = originX;
        this.originY = originY;
        this.halfWidth = (int) canvas.getWidth() / 2;
        this.halfHeight = (int) canvas.getHeight() / 2;
        this.palette = palette;
        for (int x = -halfWidth; x < halfWidth; x += chunkSize) {
            for (int y = -halfHeight; y < halfHeight; y += chunkSize) {
                DrawTask task = new DrawTask(canvas.getSet(), x - originX, y - originY, chunkSize);
                task.valueProperty().addListener((_, _, vals) -> draw(vals));
                drawTasks.add(task);
            }
        }
    }

    private void draw(Map<Long, Integer> values) {
        if (cancelled)
            return;
        Platform.runLater(() -> {
            GraphicsContext gr = canvas.getGraphicsContext2D();
            PixelWriter writer = gr.getPixelWriter();
            values.forEach((pix, val) -> drawPixel(writer, pix, val));
        });
    }

    private void drawPixel(PixelWriter writer, long pixel, int value) {
        writer.setColor(toScreenX(pixel) + halfWidth + originX, toScreenY(pixel) + halfHeight +originY, palette.getColor(value));
    }

    public void start() {
        drawTasks.forEach(exec::execute);
    }

    public void cancel() {
        cancelled = true;
        drawTasks.forEach(Task::cancel);
    }
}
