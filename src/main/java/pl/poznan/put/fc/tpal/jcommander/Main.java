package pl.poznan.put.fc.tpal.jcommander;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pl.poznan.put.fc.tpal.jcommander.config.Props;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.tasks.WatchDirTask;
import pl.poznan.put.fc.tpal.jcommander.utils.DialogUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.WindowsUtils;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private Thread watcherThread;

    public static void main(String[] args) throws IOException {
        Props.loadPreferences();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JCommander");
        primaryStage.setScene(WindowsUtils.loadScene());

        primaryStage.setWidth(Props.width);
        primaryStage.setHeight(Props.height);

        if (Props.x >= 0 && Props.y >= 0) {
            primaryStage.setX(Props.x);
            primaryStage.setY(Props.y);
        }

        Main.primaryStage = primaryStage;

        watcherThread = new Thread(() -> {
            try {
                WatchDirTask.getInstance().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        watcherThread.start();

        //primaryStage.setMaximized(true);
        primaryStage.show();

        if (!Props.isStorageOk) {
            DialogUtil.propsAlert();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        watcherThread.interrupt();
        Props.width = primaryStage.getWidth();
        Props.height = primaryStage.getHeight();
        Props.x = primaryStage.getX();
        Props.y = primaryStage.getY();
        Props.savePreferences();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
