package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

/**
 * @author Kamil Walkowiak
 */
public class ProgressDialogController {
    private Task<Void> task;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> task.cancel());
    }

    public void setTask(Task<Void> task) {
        this.task = task;
        progressBar.progressProperty().bind(task.progressProperty());
    }
}
