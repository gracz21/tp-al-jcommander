package pl.poznan.put.fc.tpal.jcommander.fileOperation;

import javafx.beans.property.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public abstract class FilesOperation extends SimpleFileVisitor<Path> {
    protected ReadOnlyLongWrapper progress;
    protected List<File> files;
    protected BooleanProperty isCanceledProperty;

    public FilesOperation(List<File> files, BooleanProperty isCanceledProperty) {
        this.files = files;
        this.isCanceledProperty = isCanceledProperty;
        this.progress = new ReadOnlyLongWrapper(this, "progress");
    }

    public long getProgress() {
        return progress.get();
    }

    public ReadOnlyLongWrapper progressProperty() {
        return progress;
    }

    public abstract void execute() throws IOException;
}
