package pl.poznan.put.fc.tpal.jcommander.fileOperation;

import javafx.beans.property.BooleanProperty;

import java.io.File;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class CopyFiles extends FilesOperation {
    public CopyFiles(List<File> files, BooleanProperty isCanceledProperty) {
        super(files, isCanceledProperty);
    }

    @Override
    public void execute() {

    }
}
