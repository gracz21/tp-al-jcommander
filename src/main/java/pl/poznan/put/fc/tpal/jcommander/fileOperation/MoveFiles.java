package pl.poznan.put.fc.tpal.jcommander.fileOperation;

import javafx.beans.property.BooleanProperty;

import java.io.File;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class MoveFiles extends FilesOperation {
    public MoveFiles(List<File> files, BooleanProperty isCanceledProperty) {
        super(files, isCanceledProperty);
    }

    @Override
    public void execute() {

    }
}
