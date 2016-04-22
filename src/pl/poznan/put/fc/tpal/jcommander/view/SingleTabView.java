package pl.poznan.put.fc.tpal.jcommander.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.poznan.put.fc.tpal.jcommander.controller.SingleTabController;
import pl.poznan.put.fc.tpal.jcommander.util.BundleUtil;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabView {
    private VBox layout;
    private SingleTabController controller;
    private FXMLLoader loader;

    public SingleTabView() throws IOException {
        loader = new FXMLLoader(getClass().getResource("../../../../../../../resources/SingleTabLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        layout = (VBox) loader.load();

        controller = loader.getController();
    }

    public Pane getLayout() {
        return layout;
    }

    public SingleTabController getController() {
        return controller;
    }

//    @Override
//    public void update(Observable o, Object arg) {
//        loader.setResources(BundleUtil.getInstance().getBundle());
//        try {
//            layout = (VBox) loader.load();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
}
