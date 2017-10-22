package pl.poznan.put.fc.tpal.jcommander.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class WindowsUtils {

    public static Scene loadScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowsUtils.class.getResource("/fxml/RootLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        Parent root = loader.load();

        BundleUtil.getInstance().addObserver(loader.getController());

        Scene scene = new Scene(root);
        return scene;
    }
}
