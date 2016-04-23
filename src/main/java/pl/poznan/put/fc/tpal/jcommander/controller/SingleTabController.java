package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.model.NameColumnEntry;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabController {
    private String currentPath;
    private StringProperty currentDirectory;
    @FXML
    private TableView<FileListEntry> fileList;
    @FXML
    private TableColumn<FileListEntry, NameColumnEntry> nameColumn;
    @FXML
    private TableColumn<FileListEntry, String> sizeColumn;
    @FXML
    private TableColumn<FileListEntry, String> dateColumn;
    @FXML
    private ComboBox<String> rootsComboBox;
    @FXML
    private Label sizeLabel;

    @FXML
    private void initialize() throws IOException {
        currentPath = "C:\\";
        currentDirectory = new SimpleStringProperty("C:\\");

        initializeColumns();
        initializeFileLists();
        initializeRootsComboBoxes();
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public String getCurrentDirectory() {
        return currentDirectory.get();
    }

    public StringProperty currentDirectoryProperty() {
        return currentDirectory;
    }

    private void initializeColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory("nameColumnEntry"));
        nameColumn.setCellFactory(param -> new NameColumnEntryCell());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().formattedFileDateOfCreationProperty());
    }

    private void initializeFileLists() throws IOException {
        fileList.setItems(FileOperationsUtil.listPathContent(FXCollections.observableArrayList(), "C:\\"));
        fileList.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    handleChangePath(fileListEntry.getFullFilePath(), fileListEntry.getNameColumnEntry().getFileName());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fileList.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    handleChangePath(fileListEntry.getFullFilePath(), fileListEntry.getNameColumnEntry().getFileName());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(event.getCode() == KeyCode.DELETE) {

            }
        });
    }

    private void initializeRootsComboBoxes() {
        ObservableList<String> rootsList = FXCollections.observableArrayList();
        Arrays.stream(File.listRoots()).forEach(file -> rootsList.add(file.toString()));
        rootsComboBox.setItems(rootsList);
        rootsComboBox.setValue(rootsList.get(0));

        rootsComboBox.setOnAction(event -> {
            String root = rootsComboBox.getSelectionModel().getSelectedItem();
            try {
                handleChangePath(root, root);
                //TODO
                //setSizeLabel();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setSizeLabel() {
    }

    private void handleChangePath(String path, String fileName) throws IOException {
        currentPath = path;
        currentDirectory.set(fileName);
        FileOperationsUtil.listPathContent(fileList.getItems(), path);
    }

    private static class NameColumnEntryCell extends TableCell<FileListEntry, NameColumnEntry> {
        @Override
        public void updateItem(NameColumnEntry item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                Image fxImage = item.getIcon();
                ImageView imageView = new ImageView(fxImage);
                setGraphic(imageView);
                setText(item.getFileName());
            }
        }
    }
}
