package view;

import javafx.scene.canvas.Canvas;
import model.JuliaSet;
import model.JuliaValue;
import model.Location;
import model.Movement;

public class JuliaCanvas extends Canvas {
    private final Palette palette = new Palette(JuliaValue.MAX_ITER);
    private JuliaSet set = new JuliaSet(2.0, .35, .35,0.0045);
    private Pixel origin = new Pixel(0, 0);
    private DrawService service = null;
    private Movement lastMovement = null;

    public JuliaCanvas() {
        super(800, 800);
        heightProperty().addListener(_ -> draw());
        widthProperty().addListener(_ -> draw());
    }

    public void reset() {
        set = new JuliaSet(2.0, .35, .35,0.0045);
        origin = Pixel.ZERO;
        draw();
    }

    public JuliaSet getSet() {
        return set;
    }

    public void zoom(double deltaScale) {
        Location focus = set.pixelToLocation(origin);
        set = set.zoom(deltaScale);
        origin = set.locationToPixel(focus);
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
        origin = new Pixel(origin.x() + (int)(getWidth() / 2 - x), origin.y() + (int)(getHeight() / 2 - y));
        draw();
    }

    public void draw() {
        if (set == null)
            return;
        if (getWidth() > 0 && getHeight() > 0) {
            if (service != null)
                service.cancel();
            service = new DrawService(this, origin, 512, palette);
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
