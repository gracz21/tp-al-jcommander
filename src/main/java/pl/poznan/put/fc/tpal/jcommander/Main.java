package pl.poznan.put.fc.tpal.jcommander;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.poznan.put.fc.tpal.jcommander.tasks.WatchDirTask;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;

public class Main extends Application {

    private static Stage primaryStage;
    private Thread watcherThread;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootLayout.fxml"));
        loader.setResources(BundleUtil.getInstance().getBundle());
        Parent root = loader.load();

        primaryStage.setTitle("JCommander");
        primaryStage.setScene(new Scene(root));
        Main.primaryStage = primaryStage;

        BundleUtil.getInstance().addObserver(loader.getController());

        watcherThread = new Thread(() -> {
            try {
                WatchDirTask.getInstance().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        watcherThread.start();

        //primaryStage.setMaximized(true);
        Main.primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        watcherThread.interrupt();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
