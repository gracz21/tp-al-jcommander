package pl.poznan.put.fc.tpal.jcommander.source.filesystem.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.ObservableList;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import pl.poznan.put.fc.tpal.jcommander.source.filesystem.models.FileListEntry;

/**
 * @author Kamil Walkowiak
 */
public abstract class FileOperationsUtil {

    public static String getRootSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getTotalSpace() / 1024);
    }

    public static String getRootFreeSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getFreeSpace() / 1024);
    }

    public static Long getPathListSize(List<Path> paths) throws IOException {
        long result = 0;

        for (Path path : paths) {
            if (Files.isDirectory(path)) {
                Stream<Path> stream = Files.list(path);
                result += getPathListSize(stream.collect(Collectors.toList()));
                stream.close();
            } else {
                result += Files.size(path);
            }
        }

        return result;
    }

    public static ObservableList<FileListEntry> listPathContent(ObservableList<FileListEntry> fileListEntries,
                                                                File pathContent) throws IOException {
        if (pathContent.exists()) {
            if (pathContent.isDirectory()) {
                File[] files = pathContent.listFiles(file -> !file.isHidden() && Files.isReadable(file.toPath()));
                fileListEntries.clear();
                for (File file : files) {
                    Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                    fileListEntries.add(new FileListEntry(file, icon));
                }
                if (pathContent.getParent() != null) {
                    Icon icon = FileSystemView.getFileSystemView().getSystemIcon(pathContent);
                    fileListEntries.add(new FileListEntry(new File(".."), icon));
                }
            } else {
                Desktop.getDesktop().open(pathContent);
            }
        }
        return fileListEntries;
    }

    public static String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

}
