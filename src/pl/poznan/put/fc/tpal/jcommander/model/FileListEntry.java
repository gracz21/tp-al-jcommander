package pl.poznan.put.fc.tpal.jcommander.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.poznan.put.fc.tpal.jcommander.Main;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Kamil Walkowiak
 */
public class FileListEntry {
    private StringProperty fileName;
    private StringProperty fileSize;
    private StringProperty formatedFileDateOfCreation;
    private FileTime fileDateOfCreation;
    private String fullFilePath;

    public FileListEntry(String fileName, String fileSize, FileTime fileDateOfCreation, String fullFilePath) {
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleStringProperty(fileSize);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Main.getLocale());
        this.formatedFileDateOfCreation = new SimpleStringProperty(df.format(fileDateOfCreation.toMillis()));
        this.fileDateOfCreation = fileDateOfCreation;
        this.fullFilePath = fullFilePath;
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

    public String getFormatedFileDateOfCreation() {
        return formatedFileDateOfCreation.get();
    }

    public StringProperty formatedFileDateOfCreationProperty() {
        return formatedFileDateOfCreation;
    }

    public FileTime getFileDateOfCreation() {
        return fileDateOfCreation;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }
}
