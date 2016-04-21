package pl.poznan.put.fc.tpal.jcommander.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.poznan.put.fc.tpal.jcommander.Main;
import pl.poznan.put.fc.tpal.jcommander.controller.SingleTabController;

import java.io.IOException;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabView {
    private VBox layout;
    private SingleTabController controller;

    public SingleTabView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../../../resources/SingleTabLayout.fxml"));
        loader.setResources(Main.getBundle());
        layout = (VBox) loader.load();

        controller = loader.getController();
    }

    public Pane getLayout() {
        return layout;
    }

    public SingleTabController getController() {
        return controller;
    }
}
