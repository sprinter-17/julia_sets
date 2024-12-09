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
    private final ObservableList<ColourBand> bandList;

    public PaletteEditor(List<ColourBand> bands, Consumer<Palette> paletteConsumer) {
        this.bands.addAll(bands);
        this.bandList = FXCollections.observableList(bands);
        setTitle("Palette Editor");
        ListView<ColourBand> bandListView = new ListView<>();
        bandListView.setCellFactory(_ -> new BandCell());
        bandListView.setItems(bandList);
        bandListView.setPrefWidth(300);
        bandListView.setPrefHeight(500);
        BorderPane pane = new BorderPane(bandListView);
        pane.setTop(tools(paletteConsumer));
        setScene(new Scene(pane));
    }

    private HBox tools(Consumer<Palette> paletteConsumer) {
        HBox tools = new HBox(5);
        Button updateButton = new Button("Show");
        updateButton.setOnAction(_ -> paletteConsumer.accept(new Palette(bands)));
        Button closeButton = new Button("Close");
        closeButton.setOnAction(_ -> hide());
        tools.getChildren().addAll(updateButton, closeButton);
        return tools;
    }

    private static class BandCell extends ListCell<ColourBand> {

        @Override
        protected void updateItem(ColourBand band, boolean empty) {
            super.updateItem(band, empty);
            if (band != null) {
                HBox box = new HBox(5);
                ColorPicker picker = new ColorPicker(band.getColour());
                picker.setOnAction(_ -> band.setColour(picker.getValue()));
                box.getChildren().addAll(picker, new Label(Integer.toString(band.getWidth())));
                setGraphic(box);
            }
        }
    }
}