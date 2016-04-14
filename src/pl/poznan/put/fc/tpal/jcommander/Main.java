package pl.poznan.put.fc.tpal.jcommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.poznan.put.fc.tpal.jcommander.controller.RootController;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RootLayout.fxml"));
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("strings", locale);
        loader.setResources(bundle);
        Parent root = loader.load();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));

        RootController controller = loader.getController();
        controller.setupData();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
