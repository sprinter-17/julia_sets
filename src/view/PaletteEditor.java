package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PaletteEditor extends Stage {
    private final ArrayList<ColourBand> bands = new ArrayList<>();
    private final Consumer<Palette> paletteConsumer;

    public PaletteEditor(List<ColourBand> bands, Consumer<Palette> paletteConsumer) {
        this.bands.addAll(bands);
        this.paletteConsumer = paletteConsumer;
        ObservableList<ColourBand> bandList = FXCollections.observableList(bands);
        setTitle("Palette Editor");
        ListView<ColourBand> bandListView = new ListView<>();
        bandListView.setCellFactory(_ -> new BandCell(this::update));
        bandListView.setItems(bandList);
        bandListView.setPrefWidth(300);
        bandListView.setPrefHeight(500);
        BorderPane pane = new BorderPane(bandListView);
        pane.setTop(tools());
        setScene(new Scene(pane));
    }

    private HBox tools() {
        HBox tools = new HBox(5);
        Button updateButton = new Button("Show");
        updateButton.setOnAction(_ -> update());
        Button closeButton = new Button("Close");
        closeButton.setOnAction(_ -> hide());
        tools.getChildren().addAll(updateButton, closeButton);
        return tools;
    }

    private void update() {
        paletteConsumer.accept(new Palette(bands));
    }

    private static class BandCell extends ListCell<ColourBand> {
        private final Runnable updater;

        public BandCell(Runnable updater) {
            this.updater = updater;
        }

        @Override
        protected void updateItem(ColourBand band, boolean empty) {
            super.updateItem(band, empty);
            if (band != null) {
                HBox box = new HBox(5);
                ColorPicker picker = new ColorPicker(band.getColour());
                picker.setOnAction(_ -> {
                    band.setColour(picker.getValue());
                    updater.run();
                });
                Spinner<Integer> widthSpinner = new Spinner<>(1, 100000, band.getWidth());
                widthSpinner.setEditable(true);
                widthSpinner.valueProperty().addListener(_ -> {
                    band.setWidth(widthSpinner.getValue());
                    updater.run();
                });
                box.getChildren().addAll(picker, widthSpinner);
                setGraphic(box);
            }
        }
    }
}
