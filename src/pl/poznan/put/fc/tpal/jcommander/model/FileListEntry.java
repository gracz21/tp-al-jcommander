package pl.poznan.put.fc.tpal.jcommander.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * @author Kamil Walkowiak
 */
public class FileListEntry {
    private StringProperty fileName;
    private StringProperty fileSize;
    private FileTime fileDateOfCreation;
    private Path fullFilePath;

    public FileListEntry(String fileName, String fileSize, FileTime fileDateOfCreation) {
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleStringProperty(fileSize);
        this.fileDateOfCreation = fileDateOfCreation;
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize.get();
    }

    public StringProperty fileSizeProperty() {
        return fileSize;
    }

    public FileTime getFileDateOfCreation() {
        return fileDateOfCreation;
    }

    public Path getFullFilePath() {
        return fullFilePath;
    }
}
