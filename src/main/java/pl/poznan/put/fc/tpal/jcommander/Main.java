package pl.poznan.put.fc.tpal.jcommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.poznan.put.fc.tpal.jcommander.controllers.RootController;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;

import java.io.IOException;

public class Main extends Application {
    private FXMLLoader loader;
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        Parent root = loader.load();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));
        Main.primaryStage = primaryStage;

        BundleUtil.getInstance().addObserver(loader.getController());

        //primaryStage.setMaximized(true);
        Main.primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
