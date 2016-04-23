package pl.poznan.put.fc.tpal.jcommander.util;

import javafx.collections.ObservableList;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public abstract class FileOperationsUtil {

    public static String getRootSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getTotalSpace()/1024);
    }

    public static String getRootFreeSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getFreeSpace()/1024);
    }

    public static Long getFileListSize(List<File> files) {
        long result = 0;

        for(File file: files) {
            if(file.isDirectory()) {
                result += getFileListSize(Arrays.asList(file.listFiles()));
            } else {
                result += file.length();
            }
        }

        return result;
    }

    public static ObservableList<FileListEntry> listPathContent(ObservableList<FileListEntry> fileListEntries, String path) throws IOException {
        File pathContent = new File(path);
        if(pathContent.exists()) {
            if(pathContent.isDirectory()) {
                File[] files = pathContent.listFiles(file -> !file.isHidden() && Files.isReadable(file.toPath()));
                fileListEntries.clear();
                if(pathContent.getParent() != null) {
                    File parent = pathContent.getParentFile();
                    Icon icon = FileSystemView.getFileSystemView().getSystemIcon(parent);
                    BasicFileAttributes attr = Files.readAttributes(parent.toPath(), BasicFileAttributes.class);
                    fileListEntries.add(new FileListEntry("..", "<DIR>", attr.creationTime(), parent, icon));
                }
                getDirectoryContent(files, fileListEntries);
            } else {
                getFileContent(pathContent);
            }
        } else {
            //TODO Dialog or nothing
            System.out.println("WTF");
        }

        return fileListEntries;
    }

    private static void getDirectoryContent(File[] files, ObservableList<FileListEntry> fileListEntries) throws IOException {
        BasicFileAttributes attr;
        for(File file : files) {
            Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            if(file.isFile()) {
                attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                fileListEntries.add(new FileListEntry(file.getName(), Long.toString(file.length()), attr.creationTime(), file, icon));
            } else {
                attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                fileListEntries.add(new FileListEntry(file.getName(), "<DIR>", attr.creationTime(), file, icon));
            }
        }
    }

    private static void getFileContent(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

}
