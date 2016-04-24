package pl.poznan.put.fc.tpal.jcommander.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import pl.poznan.put.fc.tpal.jcommander.fileOperation.CopyFiles;
import pl.poznan.put.fc.tpal.jcommander.fileOperation.DeleteFiles;
import pl.poznan.put.fc.tpal.jcommander.fileOperation.FilesOperation;
import pl.poznan.put.fc.tpal.jcommander.fileOperation.MoveFiles;
import pl.poznan.put.fc.tpal.jcommander.model.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.model.NameColumnEntry;
import pl.poznan.put.fc.tpal.jcommander.util.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.util.FileOperationsUtil;
import pl.poznan.put.fc.tpal.jcommander.view.ProgressDialogView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabController {
    private String currentPath;
    private StringProperty currentDirectory;
    private StringProperty parentPath;
    private boolean canBeDropped;
    private boolean copy;

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
    private Button upButton;
    @FXML
    private Button rootButton;
    @FXML
    private Label sizeLabel;

    @FXML
    private void initialize() throws IOException {
        currentPath = "C:\\";
        currentDirectory = new SimpleStringProperty("C:\\");
        parentPath = new SimpleStringProperty("");
        copy = false;

        initializeColumns();
        initializeFileLists();
        initializeRootsComboBoxes();
        setupDragAndDrop();
    }

    @FXML
    private void handleUpButton() throws IOException {
        if(!parentPath.get().equals("")) {
            handleChangePath(new File(parentPath.get()));
        }
    }

    @FXML
    private void handleRootButton() throws IOException {
        handleChangePath(new File(rootsComboBox.getValue()));
//        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("C:\\Users\\Kamil\\Desktop\\Test"));
//        paths.add(Paths.get("C:\\Users\\Kamil\\Desktop\\Test2.txt"));
//        (new CopyFiles(null, null, paths, Paths.get("C:\\Users\\Kamil\\Desktop\\Dest"))).execute();
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
        fileList.setItems(FileOperationsUtil.listPathContent(FXCollections.observableArrayList(), new File(currentPath),
                parentPath));
        fileList.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    if(fileListEntry != null) {
                        handleChangePath(fileListEntry.getFile());
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    handleChangePath(fileListEntry.getFile());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(event.getCode() == KeyCode.DELETE) {
                try {
                    handleDeleteAction();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileList.getSortOrder().add(sizeColumn);
        fileList.sort();
    }

    private void initializeRootsComboBoxes() {
        ObservableList<String> rootsList = FXCollections.observableArrayList();
        Arrays.stream(File.listRoots()).forEach(file -> rootsList.add(file.toString()));
        rootsComboBox.setItems(rootsList);
        rootsComboBox.setValue(rootsList.get(0));
        setSizeLabel(rootsList.get(0));

        rootsComboBox.setOnAction(event -> {
            String root = rootsComboBox.getSelectionModel().getSelectedItem();
            try {
                handleChangePath(new File(root));
                setSizeLabel(root);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupDragAndDrop() {
        canBeDropped = true;

        fileList.setOnDragDetected(event -> {
            List<FileListEntry> selected = fileList.getSelectionModel().getSelectedItems();
            if(selected.size() != 0) {
                Dragboard db = fileList.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putFiles(selected.stream().map(FileListEntry::getFile).collect(Collectors.toList()));
                db.setContent(content);
                event.consume();
            }
        });

        fileList.setOnDragOver(event -> {
            if (event.getGestureSource() != fileList && event.getDragboard().hasFiles() && canBeDropped){
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        fileList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                List<File> files = db.getFiles();
                List<Path> paths = files.stream().map(File::getPath).map(Paths::get).collect(Collectors.toList());

                BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);

                FilesOperation filesOperation;
                if(event.getTransferMode() == TransferMode.COPY) {
                    filesOperation = new CopyFiles(null, isCanceledProperty, paths, Paths.get(currentPath));
                } else {
                    filesOperation = new MoveFiles(null, isCanceledProperty, paths, Paths.get(currentPath));
                }

                Task<Void> moveTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
//                        moveFiles.progressProperty().addListener((observable, oldValue, newValue) -> {
//                            updateProgress(moveFiles.getProgress(), toDeleteSize);
//                        });
                        filesOperation.execute();
                        return null;
                    }
                };

                ProgressDialogView progressDialog = null;
                try {
                    progressDialog = new ProgressDialogView();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                progressDialog.setTask(moveTask);
                ProgressDialogView finalProgressDialog = progressDialog;
                moveTask.setOnCancelled(event2 -> {
                    isCanceledProperty.set(true);
                    finalProgressDialog.close();
                });
                moveTask.setOnSucceeded(event2 -> finalProgressDialog.close());
                progressDialog.show();
                new Thread(moveTask).start();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setSizeLabel(String root) {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        sizeLabel.setText(FileOperationsUtil.getRootFreeSpace(root) + " k " + bundle.getString("from") + " " +
            FileOperationsUtil.getRootSpace(root) + " k " + bundle.getString("free"));
    }

    private void handleChangePath(File file) throws IOException {
        currentPath = file.getPath();
        currentDirectory.set(file.getName());
        FileOperationsUtil.listPathContent(fileList.getItems(), file, parentPath);
        fileList.sort();
    }

    private void handleDeleteAction() throws IOException {
        Alert alert = setupDeleteDialog();

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            ObservableList<FileListEntry> fileListEntries = fileList.getSelectionModel().getSelectedItems();
            List<File> filesToDelete = fileListEntries.stream().
                    map(FileListEntry::getFile).collect(Collectors.toList());
            long toDeleteSize = FileOperationsUtil.getFileListSize(filesToDelete);

            BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);
            Task<Void> deleteTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    DeleteFiles deleteFiles = new DeleteFiles(filesToDelete, isCanceledProperty);
                    deleteFiles.progressProperty().addListener((observable, oldValue, newValue) -> {
                        updateProgress(deleteFiles.getProgress(), toDeleteSize);
                    });
                    deleteFiles.execute();
                    return null;
                }
            };

            ProgressDialogView progressDialog = new ProgressDialogView();
            progressDialog.setTask(deleteTask);
            deleteTask.setOnCancelled(event -> {
                isCanceledProperty.set(true);
                progressDialog.close();
            });
            deleteTask.setOnSucceeded(event -> progressDialog.close());
            progressDialog.show();
            new Thread(deleteTask).start();
        }
    }

    private Alert setupDeleteDialog() {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        Alert result = new Alert(Alert.AlertType.WARNING);
        result.setTitle(bundle.getString("warningTitle"));
        result.setHeaderText(bundle.getString("deleteHeader"));
        result.setContentText(bundle.getString("deleteWarning"));
        result.getButtonTypes().add(ButtonType.CANCEL);

        Button button = (Button) result.getDialogPane().lookupButton(ButtonType.OK);
        button.setDefaultButton(false);
        button.setText(bundle.getString("yesOption"));

        button = (Button) result.getDialogPane().lookupButton(ButtonType.CANCEL);
        button.setDefaultButton(true);
        button.setText(bundle.getString("noOption"));

        return result;
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
