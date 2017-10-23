package pl.poznan.put.fc.tpal.jcommander.source.filesystem.fileOperations.changeFileDirOperations;

import javafx.beans.property.BooleanProperty;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author Kamil Walkowiak
 */
public class CopyFile extends ChangeFileDirOperation {

    public CopyFile(List<Path> paths, BooleanProperty isCanceledProperty, Path targetPath) {
        super(paths, isCanceledProperty, targetPath);
    }

    @Override
    void changeFileDirOperation(Path file, Path destination, boolean replace) throws IOException {
        if (replace) {
            Files.copy(file, destination, REPLACE_EXISTING);
        } else {
            Files.copy(file, destination);
        }
    }

    @Override
    void keepBoth(Path file, Path destination, String fileName) throws IOException {
        String baseFileName = FilenameUtils.getBaseName(fileName);
        String fileExtension = FilenameUtils.getExtension(fileName);
        Path fileCopy = file.resolveSibling(baseFileName + "_copy" + "." + fileExtension);
        Files.copy(file, currentTargetPath.resolve(currentSourcePath.relativize(fileCopy).toString()), REPLACE_EXISTING);
    }
}
