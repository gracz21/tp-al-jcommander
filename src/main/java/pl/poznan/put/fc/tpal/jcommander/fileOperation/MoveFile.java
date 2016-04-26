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
public class MoveFile extends FileOperation {
    private List<Path> sourcePaths;
    private Path targetPath;
    private Path currentSourcePath;
    private Path currentTargetPath;

    public MoveFile(List<File> files, BooleanProperty isCanceledProperty, List<Path> sourcePaths, Path targetPath) {
        super(files, isCanceledProperty);
        this.sourcePaths = sourcePaths;
        this.targetPath = targetPath;
    }

    @Override
    void execute() throws IOException {
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
            Files.move(file, currentTargetPath.resolve(currentSourcePath.relativize(file)));
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if(!isCanceledProperty.get()) {
            Files.delete(dir);
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }
}
