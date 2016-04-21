package pl.poznan.put.fc.tpal.jcommander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    private static Locale locale = new Locale("pl");
    private static ResourceBundle bundle = ResourceBundle.getBundle("strings", locale);

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../../resources/RootLayout.fxml"));
        loader.setResources(bundle);
        Parent root = loader.load();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }
}
