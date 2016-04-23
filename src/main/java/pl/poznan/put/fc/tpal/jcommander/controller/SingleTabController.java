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
import pl.poznan.put.fc.tpal.jcommander.util.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameColumnEntry"));
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
                handleDeleteAction();
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

    private void handleDeleteAction() {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(bundle.getString("warningTitle"));
        alert.setHeaderText(bundle.getString("deleteHeader"));
        alert.setContentText("Careful with the next step!");
        alert.getButtonTypes().add(ButtonType.CANCEL);

        Button button = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        button.setDefaultButton(false);
        button.setText(bundle.getString("deleteOption"));

        button = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        button.setDefaultButton(true);
        button.setText(bundle.getString("cancelOption"));


        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            ObservableList<FileListEntry> fileListEntries = fileList.getSelectionModel().getSelectedItems();
            List<String> pathsToDelete = fileListEntries.stream().
                    map(FileListEntry::getFullFilePath).collect(Collectors.toList());
            FileOperationsUtil.deletePathContent(pathsToDelete);
        }
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
