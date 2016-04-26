package pl.poznan.put.fc.tpal.jcommander.fileOperation;

import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;
import pl.poznan.put.fc.tpal.jcommander.view.ProgressDialogView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class FileOperationTask extends Task<Void> {
    private FileOperation fileOperation;
    private List<File> fileList;
    private BooleanProperty isCanceledProperty;
    private ProgressDialogView progressDialog;

    public FileOperationTask(FileOperation fileOperation, List<File> fileList, BooleanProperty isCanceledProperty) throws IOException {
        this.fileOperation = fileOperation;
        this.fileList = fileList;
        this.isCanceledProperty = isCanceledProperty;
        this.progressDialog = new ProgressDialogView();

        progressDialog.setTask(this);
        this.setOnCancelled(event -> {
            this.isCanceledProperty.set(true);
            progressDialog.close();
        });
        this.setOnSucceeded(event -> progressDialog.close());
        progressDialog.show();
    }

    @Override
    protected Void call() throws Exception {
        long fileListSize = FileOperationsUtil.getFileListSize(fileList);

        fileOperation.progressProperty().addListener((observable, oldValue, newValue) -> {
            updateProgress(fileOperation.getProgress(), fileListSize);
        });

        fileOperation.execute();
        return null;
    }
}
