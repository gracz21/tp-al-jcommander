package pl.poznan.put.fc.tpal.jcommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.poznan.put.fc.tpal.jcommander.util.BundleUtil;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application implements Observer {
    private FXMLLoader loader;
    private Parent root;
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        Parent root = loader.load();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));
        Main.primaryStage = primaryStage;

        BundleUtil.getInstance().addObserver(this);

        //primaryStage.setMaximized(true);
        Main.primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void update(Observable o, Object arg) {
        loader.setResources(BundleUtil.getInstance().getBundle());
        try {
            loader.setRoot(null);
            loader.setController(null);
            root = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Main.primaryStage.getScene().setRoot(root);
    }
}
