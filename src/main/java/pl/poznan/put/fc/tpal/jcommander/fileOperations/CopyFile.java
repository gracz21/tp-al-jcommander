package pl.poznan.put.fc.tpal.jcommander.fileOperations;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import org.apache.commons.io.FilenameUtils;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.DialogUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.ReplaceOptionsUtil;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author Kamil Walkowiak
 */
public class CopyFile extends FileOperation {
    private Path targetPath;
    private Path currentSourcePath;
    private Path currentTargetPath;
    private Boolean replaceAll;

    public CopyFile(List<Path> paths, BooleanProperty isCanceledProperty, Path targetPath) {
        super(paths, isCanceledProperty);
        this.targetPath = targetPath;
    }

    @Override
    public void execute() throws IOException {
        if(Files.isWritable(targetPath)) {
            for(Path path : paths) {
                if(isCanceledProperty.get()) {
                    break;
                }
                currentSourcePath = path;
                currentTargetPath = Paths.get(targetPath.toString(), currentSourcePath.getFileName().toString());
                Files.walkFileTree(path, this);
                try {
                    Thread.sleep(4000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Platform.runLater(DialogUtil::noWriteAccessDialog);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(!isCanceledProperty.get()) {
            Path targetDir = currentTargetPath.resolve(currentSourcePath.relativize(dir));
            if(!Files.exists(dir)) {
                Files.copy(dir, targetDir);
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(!isCanceledProperty.get()) {
            Path targetPath = currentTargetPath.resolve(currentSourcePath.relativize(file).toString());
            if(!Files.exists(targetPath)) {
                Files.copy(file, targetPath);
            } else {
                if(replaceAll == null) {
                    String fileName = file.getFileName().toString();
                    String[] sizes = new String[]{Long.toString(Files.size(targetPath)), Long.toString(Files.size(file))};
                    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT,
                            BundleUtil.getInstance().getCurrentLocale());
                    String[] dates = new String[]{df.format(Files.getLastModifiedTime(file).toMillis()),
                            df.format(Files.getLastModifiedTime(targetPath).toMillis())};

                    FutureTask<ReplaceOptionsUtil> dialog =
                            new FutureTask<>(() -> DialogUtil.replaceDialog(fileName, sizes, dates));
                    Platform.runLater(dialog);

                    try {
                        switch(dialog.get()) {
                            case YES:
                                Files.copy(file, targetPath, REPLACE_EXISTING);
                                break;
                            case KEEP:
                                String baseFileName = FilenameUtils.getBaseName(fileName);
                                String fileExtension = FilenameUtils.getExtension(fileName);
                                Path fileCopy = file.resolveSibling(baseFileName + "_copy" + "." + fileExtension);
                                Files.copy(file, currentTargetPath.resolve(currentSourcePath.relativize(fileCopy).toString()), REPLACE_EXISTING);
                                break;
                            case NO:
                                break;
                            case YES_ALL:
                                Files.copy(file, targetPath, REPLACE_EXISTING);
                                replaceAll = Boolean.TRUE;
                                break;
                            case KEEP_ALL:
                                break;
                            case NO_ALL:
                                replaceAll = Boolean.FALSE;
                                break;
                            case CANCEL:
                                return TERMINATE;
                        }
                    } catch(InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    if(replaceAll) {
                        Files.copy(file, targetPath, REPLACE_EXISTING);
                    }
                }
            }
            progress.set(progress.getValue() + Files.size(file));
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }
}
