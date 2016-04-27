package pl.poznan.put.fc.tpal.jcommander.tasks;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author Kamil Walkowiak
 */
public class WatchDirTask extends java.util.Observable implements ChangeListener {
    private List<StringProperty> currentDirProperties;
    private WatchService watcher;
    private List<WatchKey> watchKeys;

    public WatchDirTask(StringProperty leftDir, StringProperty rightDir) throws IOException {
        this.currentDirProperties = new LinkedList<>();
        this.currentDirProperties.add(leftDir);
        this.currentDirProperties.add(rightDir);
        this.watchKeys = new LinkedList<>();
        this.watcher = FileSystems.getDefault().newWatchService();

        for(StringProperty currentDirProperty: currentDirProperties) {
            Path dir = Paths.get(currentDirProperty.get());
            watchKeys.add(dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY));
        }
    }

    protected Void call() throws Exception {
        currentDirProperties.stream().forEach(stringProperty -> stringProperty.addListener(this));

        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                return null;
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }

        return null;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

    }
}
