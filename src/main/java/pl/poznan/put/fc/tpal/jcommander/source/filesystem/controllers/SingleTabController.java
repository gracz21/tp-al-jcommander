package pl.poznan.put.fc.tpal.jcommander.source.filesystem.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.comparators.NameComparator;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.comparators.SizeComparator;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.fileOperations.DeleteFile;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.fileOperations.FileOperation;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.fileOperations.changeFileDirOperations.CopyFile;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.fileOperations.changeFileDirOperations.MoveFile;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.models.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.models.NameColumnEntry;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.tasks.FileOperationTask;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.DialogUtil;
import pl.poznan.put.fc.tpal.jcommander.source.filesystem.utils.FileOperationsUtil;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabController implements Observer {

    private StringProperty currentPath;
    private StringProperty currentDirectory;
    private boolean canBeDropped;

    public StringProperty currentDirectoryProperty() {
        return currentDirectory;
    }

    public StringProperty currentPathProperty() {
        return currentPath;
    }

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
    private TextField pathTextField;

    @FXML
    private void initialize() throws IOException {
        String userHomeDirectory = FileOperationsUtil.getUserHomeDirectory();
        currentPath = new SimpleStringProperty(userHomeDirectory);
        currentDirectory = new SimpleStringProperty(userHomeDirectory);

        rootButton.setText("r00t");

        pathTextField.setText(userHomeDirectory);
        pathTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    handleChangePath(new File(pathTextField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //TODO tab - autocomplete
        });

        initializeColumns();
        initializeFileLists();
        initializeRootsComboBoxes();
        setupDragAndDrop();
    }

    private void initializeColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameColumnEntry"));
        nameColumn.setCellFactory(param -> new NameColumnEntryCell());
        nameColumn.setComparator(new NameComparator());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty());
        sizeColumn.setComparator(new SizeComparator());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().formattedFileDateOfCreationProperty());
    }

    private void initializeFileLists() throws IOException {
        fileList.setItems(FileOperationsUtil
                .listPathContent(FXCollections.observableArrayList(), new File(currentPath.get())));
        fileList.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    if (fileListEntry != null) {
                        if (fileListEntry.getNameColumnEntry().getFileName().equals("..")) {
                            File file = new File(currentPath.get()).getParentFile();
                            handleChangePath(file);
                        } else {
                            handleChangePath(fileListEntry.getFile());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    if (fileListEntry != null) {
                        handleChangePath(fileListEntry.getFile());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (event.getCode() == KeyCode.DELETE) {
                try {
                    if (fileList.getSelectionModel().getSelectedCells().size() != 0) {
                        handleDeleteAction();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (event.getCode() == KeyCode.BACK_SPACE) {
                try {
                    handleUpButton();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileList.getSortOrder().add(nameColumn);
        fileList.sort();
    }

    private void handleDeleteAction() throws IOException {
        if (DialogUtil.deleteDialog()) {
            ObservableList<FileListEntry> fileListEntries = fileList.getSelectionModel().getSelectedItems();
            List<Path> pathsToDelete = fileListEntries.stream().
                    map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());

            BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);
            DeleteFile deleteFiles = new DeleteFile(pathsToDelete, isCanceledProperty);
            new Thread(new FileOperationTask(deleteFiles, isCanceledProperty)).start();
//            FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()), parentPath);
//            fileList.sort();
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupDragAndDrop() {
        canBeDropped = true;

        fileList.setOnDragDetected(event -> {
            List<FileListEntry> selected = fileList.getSelectionModel().getSelectedItems();
            if (selected.size() != 0) {
                Dragboard db = fileList.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putFiles(selected.stream().map(FileListEntry::getFile).collect(Collectors.toList()));
                db.setContent(content);
                event.consume();
            }
        });

        fileList.setOnDragOver(event -> {
            if (event.getGestureSource() != fileList && event.getDragboard().hasFiles() && canBeDropped) {
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

                FileOperation fileOperation;
                if (event.getTransferMode() == TransferMode.COPY) {
                    fileOperation = new CopyFile(paths, isCanceledProperty, Paths.get(currentPath.get()));
                } else {
                    fileOperation = new MoveFile(paths, isCanceledProperty, Paths.get(currentPath.get()));
                }

                try {
                    new Thread(new FileOperationTask(fileOperation, isCanceledProperty)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    private void handleUpButton() throws IOException {
        if (!currentPath.get().equals("")) {
            File file = new File(currentPath.get());
            if (file.getParent() != null) {
                handleChangePath(new File(file.getParent()));
            }
        }
    }

    @FXML
    private void handleRootButton() throws IOException {
        handleChangePath(new File(rootsComboBox.getValue()));
    }

    private void handleChangePath(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                currentPath.set(file.getPath());
                currentDirectory.set(file.getName());
                if (currentDirectory.get().equals("")) {
                    currentDirectory.set(rootsComboBox.getValue());
                }
                pathTextField.setText(currentPath.get());
                FileOperationsUtil.listPathContent(fileList.getItems(), file);
                fileList.sort();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            ResourceBundle bundle = BundleUtil.getInstance().getBundle();

            nameColumn.setText(bundle.getString("fileList.columns.name"));
            sizeColumn.setText(bundle.getString("fileList.columns.size"));
            dateColumn.setText(bundle.getString("fileList.columns.date"));

            setSizeLabel(rootsComboBox.getSelectionModel().getSelectedItem());

            try {
                FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.sort();
        } else {
            System.out.println(currentPath.get());
            System.out.println(((StringProperty) arg).get());
            System.out.println();
            if (((StringProperty) arg).get().equals(currentPath.get())) {
                try {
                    System.out.println("In");
                    FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileList.sort();
            }
        }
    }

    private void setSizeLabel(String root) {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        sizeLabel.setText(
                FileOperationsUtil.getRootFreeSpace(root) + " Kb " + bundle.getString("from") + " " + FileOperationsUtil
                        .getRootSpace(root) + " Kb " + bundle.getString("free"));
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
