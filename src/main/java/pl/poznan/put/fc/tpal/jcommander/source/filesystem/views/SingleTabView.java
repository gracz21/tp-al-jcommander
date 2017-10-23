package pl.poznan.put.fc.tpal.jcommander.source.filesystem.views;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.controllers.SingleTabController;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabView {

    private VBox layout;
    private SingleTabController controller;

    public SingleTabView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/filesystem/SingleTabLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        layout = loader.load();

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
