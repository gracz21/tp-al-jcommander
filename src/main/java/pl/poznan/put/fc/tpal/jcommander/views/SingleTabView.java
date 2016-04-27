package pl.poznan.put.fc.tpal.jcommander.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.poznan.put.fc.tpal.jcommander.controllers.SingleTabController;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;

import java.io.IOException;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabView {
    private VBox layout;
    private SingleTabController controller;
    private FXMLLoader loader;

    public SingleTabView() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/SingleTabLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        layout = (VBox) loader.load();

        controller = loader.getController();
        BundleUtil.getInstance().addObserver(controller);
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
