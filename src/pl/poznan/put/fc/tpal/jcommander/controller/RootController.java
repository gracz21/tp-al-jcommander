package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class RootController {
    @FXML
    private ArrayList<TableView<FileListEntry>> fileLists;
    @FXML
    private ArrayList<TableColumn<FileListEntry, String>> nameColumns;
    @FXML
    private ArrayList<TableColumn<FileListEntry, String>> sizeColumns;
    @FXML
    private ArrayList<TableColumn<FileListEntry, Date>> dateColumns;

    @FXML
    private void initialize() throws IOException {
        nameColumns.stream().forEach(column -> column.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty()));
        sizeColumns.stream().forEach(column -> column.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty()));

        for(TableView<FileListEntry> fileList: fileLists) {
            fileList.setItems(FileOperationsUtil.listPathContent(FXCollections.observableArrayList(), "C:\\"));
            fileList.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        handleChangePath(fileList);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void handleChangePath(TableView<FileListEntry> table) throws IOException {
        FileOperationsUtil.listPathContent(table.getItems(), table.getSelectionModel().getSelectedItem().getFullFilePath());
    }
}
