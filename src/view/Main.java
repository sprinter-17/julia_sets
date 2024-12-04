package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private final JuliaCanvas canvas = new JuliaCanvas();
    Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), _ -> canvas.updateC(-0.00001, 0.0005)));

    public static void main(String... args) {
        launch(args);
    }

    private HBox tools() {
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(_ -> Platform.exit());
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> canvas.reset());
        Button powPlusButton = new Button("pow+");
        powPlusButton.setOnAction(_-> canvas.updatePow(+0.001));
        Button powMinusButton = new Button("pow-");
        powMinusButton.setOnAction(_-> canvas.updatePow(-0.001));
        Button zoomInButton = new Button("+");
        zoomInButton.setOnAction(_ -> canvas.zoom(-.5));
        Button zoomOutButton = new Button("-");
        zoomOutButton.setOnAction(_ -> canvas.zoom(+1));
        Button cxPlusButton = new Button("cx+");
        cxPlusButton.setOnAction(_ -> canvas.updateC(+0.001, 0));
        Button cxMinusButton = new Button("cx-");
        cxMinusButton.setOnAction(_ -> canvas.updateC(-0.001, 0));
        Button cyPlusButton = new Button("cy+");
        cyPlusButton.setOnAction(_ -> canvas.updateC(0, +0.001));
        Button cyMinusButton = new Button("cy-");
        cyMinusButton.setOnAction(_ -> canvas.updateC(0, -0.001));
        Button playButton = new Button("Play");
        playButton.setOnAction(this::togglePlay);
        HBox tools = new HBox(5, resetButton, zoomInButton, zoomOutButton, powPlusButton, powMinusButton, cxPlusButton, cxMinusButton, cyPlusButton, cyMinusButton, playButton, exitButton);
        tools.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return tools;
    }

    private void togglePlay(ActionEvent ev) {
        Button button = (Button)ev.getSource();
        System.out.println(button.toString());
        if (timer.getStatus() == Animation.Status.RUNNING) {
            button.setText("Play");
            timer.stop();
        } else {
            button.setText("Stop");
            timer.play();
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
