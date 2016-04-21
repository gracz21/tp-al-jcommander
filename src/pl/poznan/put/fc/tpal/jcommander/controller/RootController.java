package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RootController {
    private ArrayList<String> currentPaths;
    @FXML
    private ArrayList<TableView<FileListEntry>> fileLists;
    @FXML
    private ArrayList<TableColumn<FileListEntry, String>> nameColumns;
    @FXML
    private ArrayList<TableColumn<FileListEntry, String>> sizeColumns;
    @FXML
    private ArrayList<TableColumn<FileListEntry, String>> dateColumns;
    @FXML
    private ArrayList<ComboBox<String>> rootsComboBoxes;
    @FXML
    private Label leftSizeLabel;

    @FXML
    private void initialize() throws IOException {
        currentPaths = new ArrayList<>(Arrays.asList("C:\\", "C:\\"));

        initializeColumns();
        initializeFileLists();
        initializeRootsComboBoxes();
    }

    private void initializeColumns() {
        nameColumns.stream().forEach(column -> column.setCellValueFactory(cellData ->
                cellData.getValue().fileNameProperty()));
        sizeColumns.stream().forEach(column -> column.setCellValueFactory(cellData ->
                cellData.getValue().fileSizeProperty()));
        dateColumns.stream().forEach(column -> column.setCellValueFactory(cellData ->
                cellData.getValue().formatedFileDateOfCreationProperty()));
    }

    private void initializeFileLists() throws IOException {
        for(TableView<FileListEntry> fileList: fileLists) {
            fileList.setItems(FileOperationsUtil.listPathContent(FXCollections.observableArrayList(), "C:\\"));
            fileList.setOnMousePressed(event -> {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        handleChangePath(fileList);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            fileList.setOnKeyPressed(event -> {
                if(event.getCode() == KeyCode.ENTER) {
                    try {
                        handleChangePath(fileList);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initializeRootsComboBoxes() {
        ObservableList<String> rootsList = FXCollections.observableArrayList();
        Arrays.stream(File.listRoots()).forEach(file -> rootsList.add(file.toString()));
        rootsComboBoxes.stream().forEach(comboBox -> comboBox.setItems(rootsList));
        rootsComboBoxes.stream().forEach(comboBox -> comboBox.setValue(rootsList.get(0)));
    }

    private void setSizeLabel() {

    }

    private void handleChangePath(TableView<FileListEntry> table) throws IOException {
        FileOperationsUtil.listPathContent(table.getItems(), table.getSelectionModel().getSelectedItem().getFullFilePath());
    }
}
