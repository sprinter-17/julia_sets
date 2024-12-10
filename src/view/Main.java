package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Movement;
import java.util.List;

public class Main extends Application {
    private final List<ColourBand> bands = List.of(
            new ColourBand(Color.GOLD, 40),
            new ColourBand(Color.SALMON, 140),
            new ColourBand(Color.DARKGREEN, 200),
            new ColourBand(Color.GOLDENROD, 450),
            new ColourBand(Color.ALICEBLUE, 600),
            new ColourBand(Color.OLIVEDRAB, 6000)
    );
    private Palette palette = new Palette(bands);
    private final JuliaCanvas canvas = new JuliaCanvas(palette);
    private final PaletteEditor paletteEditor = new PaletteEditor(bands, this::setPalette);
    Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), _ -> canvas.repeatLastUpdate()));

    public static void main(String... args) {
        launch(args);
    }

    private Parent tools() {
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(_ -> exit());
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> canvas.reset());
        Button zoomInButton = new Button("+");
        zoomInButton.setOnAction(_ -> canvas.zoom(-.5));
        Button zoomOutButton = new Button("-");
        zoomOutButton.setOnAction(_ -> canvas.zoom(+1));
        Button cxPlusButton = new Button("cx+");
        cxPlusButton.setOnAction(_ -> canvas.update(new Movement(Movement.Param.CX, Movement.Dir.POSITIVE)));
        Button cxMinusButton = new Button("cx-");
        cxMinusButton.setOnAction(_ -> canvas.update(new Movement(Movement.Param.CX, Movement.Dir.NEGATIVE)));
        Button cyPlusButton = new Button("cy+");
        cyPlusButton.setOnAction(_ -> canvas.update(new Movement(Movement.Param.CY, Movement.Dir.POSITIVE)));
        Button cyMinusButton = new Button("cy-");
        cyMinusButton.setOnAction(_ -> canvas.update(new Movement(Movement.Param.CY, Movement.Dir.NEGATIVE)));
        Button playButton = new Button("Play");
        playButton.setOnAction(this::togglePlay);
        Button editPaletteButton = new Button("Palette");
        editPaletteButton.setOnAction(_ -> editPalettte());
        HBox buttons = new HBox(5, resetButton, zoomInButton, zoomOutButton,
                cxPlusButton, cxMinusButton, cyPlusButton, cyMinusButton,
                editPaletteButton, playButton,
                exitButton);
        buttons.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        Slider deltaSlider = new Slider(0.0001, 1.0, canvas.getSet().getDelta());
        deltaSlider.valueProperty().addListener((_, _, val) -> canvas.getSet().setDelta(val.doubleValue()));
        return new VBox(5, buttons, deltaSlider);
    }

    private void editPalettte() {
        paletteEditor.show();
    }

    private void setPalette(Palette palette) {
        this.palette = palette;
        canvas.setPalette(palette);
    }

    private void exit() {
        canvas.cancelDrawService();
        Platform.exit();
    }

    private void togglePlay(ActionEvent ev) {
        if (ev.getSource() instanceof Button button) {
            if (timer.getStatus() == Animation.Status.RUNNING) {
                button.setText("Play");
                timer.stop();
            } else {
                button.setText("Stop");
                timer.play();
            }
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane pane = new BorderPane(canvas);
        pane.setTop(tools());
        stage.setTitle("Julia Sets");
        stage.setScene(new Scene(pane));
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> canvas.moveTo(ev.getX(), ev.getY()));
        pane.setPrefWidth(600);
        pane.setPrefHeight(600);
        stage.show();
        timer.setCycleCount(Timeline.INDEFINITE);
    }
}
