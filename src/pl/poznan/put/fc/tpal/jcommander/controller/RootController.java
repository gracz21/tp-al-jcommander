package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;

import java.io.IOException;
import java.util.Date;

public class RootController {
    @FXML
    private TableView<FileListEntry> leftFileList;
    @FXML
    private TableView<FileListEntry> rightFileList;
    @FXML
    private TableColumn<FileListEntry, String> leftNameColumn;
    @FXML
    private TableColumn<FileListEntry, String> leftSizeColumn;
    @FXML
    private TableColumn<FileListEntry, Date> leftDateColumn;

    @FXML
    private void initialize() {
        leftNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        leftSizeColumn.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty());
    }

    public void setupData() throws IOException {
        leftFileList.setItems(FileOperationsUtil.listPathContent("C:\\"));
    }
}
