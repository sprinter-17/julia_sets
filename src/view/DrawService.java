package view;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DrawService {
    private final List<Task<Image>> drawTasks = new ArrayList<>();
    private final JuliaCanvas canvas;
    private final Executor exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });
    private boolean cancelled = false;

    public DrawService(JuliaCanvas canvas, int originX, int originY, int chunkSize, Palette palette) {
        this.canvas = canvas;
        int halfWidth = (int) canvas.getWidth() / 2;
        int halfHeight = (int) canvas.getHeight() / 2;
        for (int x = -halfWidth; x < halfWidth; x += chunkSize) {
            for (int y = -halfHeight; y < halfHeight; y += chunkSize) {
                DrawTask task = new DrawTask(canvas.getSet(), palette, x - originX, y - originY, chunkSize);
                task.valueProperty().addListener(imageDrawer(x + halfWidth, y + halfHeight));
                drawTasks.add(task);
            }
        }
    }

    private ChangeListener<Image> imageDrawer(int x, int y) {
        return (_, _, image) -> {
            if (!cancelled)
                canvas.getGraphicsContext2D().drawImage(image, x, y);
        };
    }

    public void start() {
        drawTasks.forEach(exec::execute);
    }

    public void cancel() {
        cancelled = true;
        drawTasks.forEach(Task::cancel);
    }
}
