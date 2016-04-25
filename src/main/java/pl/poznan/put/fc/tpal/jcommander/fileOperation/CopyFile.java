package pl.poznan.put.fc.tpal.jcommander.fileOperation;

import javafx.beans.property.BooleanProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

/**
 * @author Kamil Walkowiak
 */
public class CopyFile extends FileOperation {
    private List<Path> sourcePaths;
    private Path targetPath;
    private Path currentSourcePath;
    private Path currentTargetPath;

    public CopyFile(List<File> files, BooleanProperty isCanceledProperty, List<Path> sourcePaths, Path targetPath) {
        super(files, isCanceledProperty);
        this.sourcePaths = sourcePaths;
        this.targetPath = targetPath;
    }

    @Override
    public void execute() throws IOException {
        for(Path path: sourcePaths) {
            if(isCanceledProperty.get()) {
                break;
            }
            currentSourcePath = path;
            currentTargetPath = Paths.get(targetPath.toString(), currentSourcePath.getFileName().toString());
            Files.walkFileTree(path, this);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(!isCanceledProperty.get()) {
            Path targetDir = currentTargetPath.resolve(currentSourcePath.relativize(dir));
            try {
                Files.copy(dir, targetDir);
            } catch(FileAlreadyExistsException e) {
                if(!Files.isDirectory(targetDir))
                    throw e;
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(!isCanceledProperty.get()) {
            Files.copy(file, currentTargetPath.resolve(currentSourcePath.relativize(file)));
            progress.set(progress.getValue() + Files.size(file));
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }
}
