package view;

import javafx.scene.canvas.Canvas;
import model.JuliaSet;
import model.JuliaValue;
import model.Location;
import model.Movement;

public class JuliaCanvas extends Canvas {
    private final Palette palette = new Palette(JuliaValue.MAX_ITER);
    private JuliaSet set = new JuliaSet(2.0, .35, .35,0.0045);
    private int originX = 0;
    private int originY = 0;
    private DrawService service = null;
    private Movement lastMovement = null;

    public static long toPixel(int x, int y) {
        return (long)(y + 10000) << 32 | (x + 10000);
    }

    public static int toScreenX(long pixel) {
        return (int)pixel - 10000;
    }

    public static int toScreenY(long pixel) {
        return (int)(pixel >> 32) - 10000;
    }

    public JuliaCanvas() {
        super(800, 800);
        heightProperty().addListener(_ -> draw());
        widthProperty().addListener(_ -> draw());
    }

    public void reset() {
        set = new JuliaSet(2.0, .35, .35,0.0045);
        originX = 0;
        originY = 0;
        draw();
    }

    public JuliaSet getSet() {
        return set;
    }

    public void zoom(double deltaScale) {
        Location focus = set.pixelToLocation(originX, originY);
        set = set.zoom(deltaScale);
        originX = set.pixelX(focus);
        originY = set.pixelY(focus);
        draw();
    }

    public void update(Movement movement) {
        lastMovement = movement;
        set = set.update(movement);
        draw();
    }

    public void repeatLastUpdate() {
        if (lastMovement != null)
            update(lastMovement);
    }

    public void moveTo(double x, double y) {
        originX += (int)(getWidth() / 2 - x);
        originY += (int)(getHeight() / 2 - y);
        draw();
    }

    public void draw() {
        if (set == null)
            return;
        if (getWidth() > 0 && getHeight() > 0) {
            if (service != null)
                service.cancel();
            service = new DrawService(this, originX, originY, 64, palette);
            service.start();
        }
    }

    public void cancelDrawService() {
        if (service != null)
            service.cancel();
    }

    @Override
    public boolean isResizable() {
        return true;
    }
}
