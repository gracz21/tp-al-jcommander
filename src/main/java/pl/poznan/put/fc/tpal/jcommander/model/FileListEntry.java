package pl.poznan.put.fc.tpal.jcommander.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.poznan.put.fc.tpal.jcommander.Main;
import pl.poznan.put.fc.tpal.jcommander.util.BundleUtil;

import javax.swing.*;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;

/**
 * @author Kamil Walkowiak
 */
public class FileListEntry {
    private NameColumnEntry nameColumnEntry;
    private StringProperty fileSize;
    private StringProperty formattedFileDateOfCreation;
    private FileTime fileDateOfCreation;
    private String fullFilePath;


    public FileListEntry(String fileName, String fileSize, FileTime fileDateOfCreation, String fullFilePath, Icon swingIcon) {
        this.nameColumnEntry = new NameColumnEntry(fileName, swingIcon);
        this.fileSize = new SimpleStringProperty(fileSize);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, BundleUtil.getInstance().getLocale());
        this.formattedFileDateOfCreation = new SimpleStringProperty(df.format(fileDateOfCreation.toMillis()));
        this.fileDateOfCreation = fileDateOfCreation;
        this.fullFilePath = fullFilePath;
    }

    public NameColumnEntry getNameColumnEntry() {
        return nameColumnEntry;
    }

    public String getFileSize() {
        return fileSize.get();
    }

    public StringProperty fileSizeProperty() {
        return fileSize;
    }

    public String getFormattedFileDateOfCreation() {
        return formattedFileDateOfCreation.get();
    }

    public StringProperty formattedFileDateOfCreationProperty() {
        return formattedFileDateOfCreation;
    }

    public FileTime getFileDateOfCreation() {
        return fileDateOfCreation;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }
}
