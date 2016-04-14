package pl.poznan.put.fc.tpal.jcommander.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Kamil Walkowiak
 */
public abstract class FileOperationsUtil {
    public static ObservableList<FileListEntry> listPathContent(String path) throws IOException {
        ObservableList<FileListEntry> pathContent = FXCollections.observableArrayList();
        File directory = new File(path);
        if(directory.exists()) {
            if(directory.isDirectory()) {
                File[] files = directory.listFiles();
                BasicFileAttributes attr;
                for(File file : files) {
                    if(file.isFile()) {
                        Path path1 = file.toPath();
                        attr = Files.readAttributes(path1, BasicFileAttributes.class);
                        pathContent.add(new FileListEntry(file.getName(), Long.toString(file.length()), attr.creationTime()));
                    } else {
                        Path path1 = file.toPath();
                        attr = Files.readAttributes(path1, BasicFileAttributes.class);
                        pathContent.add(new FileListEntry(file.getName(), "<DIR>", attr.creationTime()));
                    }
                }
            } else {
                System.out.println("Weep");
            }
        } else {
            System.out.println("WTF");
        }

        return pathContent;
    }
}
