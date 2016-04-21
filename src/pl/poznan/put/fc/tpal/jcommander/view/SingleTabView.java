package pl.poznan.put.fc.tpal.jcommander.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.poznan.put.fc.tpal.jcommander.Main;

import java.io.IOException;
import java.net.URL;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabView {
    private VBox layout;

    public SingleTabView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../../../resources/SingleTabLayout.fxml"));
        loader.setResources(Main.getBundle());
        layout = (VBox) loader.load();
    }

    public Pane getLayout() {
        return layout;
    }
}
