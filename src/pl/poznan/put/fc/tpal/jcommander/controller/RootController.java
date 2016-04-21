package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import pl.poznan.put.fc.tpal.jcommander.view.SingleTabView;

import java.io.IOException;

public class RootController {
    @FXML
    private TabPane leftTabPane;
    @FXML
    private TabPane rightTabPane;

    @FXML
    private void initialize() throws IOException {
        SingleTabView leftFirstTab = new SingleTabView();
        SingleTabView rightFirstTab = new SingleTabView();

        BorderPane borderPane = (BorderPane) leftTabPane.getTabs().get(0).getContent();
        borderPane.setCenter(leftFirstTab.getLayout());
        leftFirstTab.getLayout().prefWidthProperty().bind(borderPane.widthProperty());
        leftFirstTab.getLayout().prefHeightProperty().bind(borderPane.heightProperty());

        borderPane = (BorderPane) rightTabPane.getTabs().get(0).getContent();
        borderPane.setCenter(rightFirstTab.getLayout());
        rightFirstTab.getLayout().prefWidthProperty().bind(borderPane.widthProperty());
        rightFirstTab.getLayout().prefHeightProperty().bind(borderPane.heightProperty());
    }
}
