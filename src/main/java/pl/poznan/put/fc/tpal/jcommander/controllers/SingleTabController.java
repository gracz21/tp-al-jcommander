package pl.poznan.put.fc.tpal.jcommander.controllers;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import pl.poznan.put.fc.tpal.jcommander.comparators.NameComparator;
import pl.poznan.put.fc.tpal.jcommander.comparators.SizeComparator;
import pl.poznan.put.fc.tpal.jcommander.tasks.FileOperationTask;
import pl.poznan.put.fc.tpal.jcommander.fileOperations.CopyFile;
import pl.poznan.put.fc.tpal.jcommander.fileOperations.DeleteFile;
import pl.poznan.put.fc.tpal.jcommander.fileOperations.FileOperation;
import pl.poznan.put.fc.tpal.jcommander.fileOperations.MoveFile;
import pl.poznan.put.fc.tpal.jcommander.models.FileListEntry;
import pl.poznan.put.fc.tpal.jcommander.models.NameColumnEntry;
import pl.poznan.put.fc.tpal.jcommander.utils.BundleUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.DialogUtil;
import pl.poznan.put.fc.tpal.jcommander.utils.FileOperationsUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kamil Walkowiak
 */
public class SingleTabController implements Observer {
    private String currentPath;
    private StringProperty currentDirectory;
    private StringProperty parentPath;
    private boolean canBeDropped;

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
    }

    public StringProperty currentDirectoryProperty() {
        return currentDirectory;
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
                    if(fileListEntry != null) {
                        handleChangePath(fileListEntry.getFile());
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(event.getCode() == KeyCode.DELETE) {
                try {
                    if(fileList.getSelectionModel().getSelectedCells().size() != 0) {
                        handleDeleteAction();
                    }
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
        Arrays.stream(File.listRoots()).forEach(file -> {
            String name = file.toString();
            if(!name.equals("A:\\")) {
                rootsList.add(file.toString());
            }
        });
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

                FileOperation fileOperation;
                if(event.getTransferMode() == TransferMode.COPY) {
                    fileOperation = new CopyFile(paths, isCanceledProperty, Paths.get(currentPath));
                } else {
                    fileOperation = new MoveFile(paths, isCanceledProperty, Paths.get(currentPath));
                }

                try {
                    new Thread(new FileOperationTask(fileOperation, isCanceledProperty)).start();
                } catch(IOException e) {
                    e.printStackTrace();
                }
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
        if(DialogUtil.deleteDialog()) {
            ObservableList<FileListEntry> fileListEntries = fileList.getSelectionModel().getSelectedItems();
            List<Path> pathsToDelete = fileListEntries.stream().
                    map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());

            BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);
            DeleteFile deleteFiles = new DeleteFile(pathsToDelete, isCanceledProperty);
            new Thread(new FileOperationTask(deleteFiles, isCanceledProperty)).start();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        nameColumn.setText(bundle.getString("fileList.columns.name"));
        sizeColumn.setText(bundle.getString("fileList.columns.size"));
        dateColumn.setText(bundle.getString("fileList.columns.date"));

        setSizeLabel(rootsComboBox.getSelectionModel().getSelectedItem());

        try {
            FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath), parentPath);
        } catch(IOException e) {
            e.printStackTrace();
        }
        fileList.sort();
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
