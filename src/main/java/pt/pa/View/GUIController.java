package pt.pa.View;

import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import pt.pa.Commands.*;
import pt.pa.Document;
import pt.pa.Folder;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;
import pt.pa.adts.TreeLinked;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The GUIController class handles user interactions and updates the graphical user interface accordingly.
 */
public class GUIController {

    private PFS model;
    private GUI gui;
    private Position<Document> previousSelection;
    private TreeItem<Document> previousItem;

    private DocumentGridManager documentGridManager;
    private List<Document> lastAddedDocuments = Collections.emptyList();
    private AlertUtils alertUtils = new AlertUtils();

    /**
     * Constructs a GUIController with the specified model and GUI components.
     *
     * @param model The underlying file system model.
     * @param gui   The graphical user interface components.
     */
    public GUIController(PFS model, GUI gui) {
        this.model = model;
        this.gui = gui;
        this.documentGridManager = new DocumentGridManager(gui.getDocumentGrid(),model,gui);
        gui.setTriggers(this);
        createTreeView(model.getPfs());
    }

    /**
     * Handles the creation of a new file.
     */
    public void doCreateFile() {
        Position<Document> selectedPosition = getSelectedPosition();
        if (selectedPosition != null) {
            try {
                CreateFileCommand createFileCommand = new CreateFileCommand(model, selectedPosition, "newFile");
                model.getCommandManager().executeCommand(createFileCommand);
                update();
            } catch (IOException e) {
                System.err.println("Error executing CreateCommand: " + e.getMessage());
                alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "File Creation Error", "An error occurred during file creation.");
            }
        } else {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error:", "File Creation Error", "Please select a valid position before creating a file.");
        }
    }

    /**
     * Handles the creation of a new folder.
     */
    public void doCreateFolder() {
        Position<Document> selectedPosition = getSelectedPosition();
        if (selectedPosition != null) {
            try {
                CreateFolderCommand createFolderCommand = new CreateFolderCommand(model, selectedPosition, "newFolder");
                model.getCommandManager().executeCommand(createFolderCommand);
                update();
            } catch (IOException e) {
                System.err.println("Error executing CreateCommand: " + e.getMessage());
                alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Folder Creation Error", "An error occurred during folder creation.");
            }
        } else {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Folder Creation Error", "Please select a valid position before creating a folder.");
        }
    }

    /**
     * Handles the copy operation.
     */
    public void doCopy() {
        try {
            if (getSelectedPosition() == null) {
                alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Copy Error", "No selected Position");
                return;
            }
            CopyCommand copyCommand = new CopyCommand(model, getSelectedPosition());
            model.getCommandManager().executeCommand(copyCommand);
        } catch (IOException e) {
            System.err.println("Error executing CopyCommand: " + e.getMessage());
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Copy Error", "An error occurred during the copy operation.");
        }
    }

    /**
     * Handles the paste operation.
     */
    public void doPaste() {
        try {
            if (getSelectedPosition() == null) {
                alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Paste Error", "No selected Position");
                return;
            }
            PasteCommand pasteCommand = new PasteCommand(model, getSelectedPosition());
            model.getCommandManager().executeCommand(pasteCommand);
            update();
        } catch (IOException e) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Paste Operation Failed", "An error occurred during the paste operation: " + e.getMessage());
        }
    }

    /**
     * Initiates the move operation.
     */
    public void doMove() {
        Position<Document> sourcePosition = getSelectedPosition();
        System.out.println("Select the destination position.");
        gui.getGuiTreeView().setOnMouseClicked(destinationMouseEvent -> {
            if (destinationMouseEvent.getClickCount() == 1) {
                Position<Document> destinationPosition = getSelectedPosition();
                if (sourcePosition != destinationPosition) {
                    try {
                        MoveCommand moveCommand = new MoveCommand(model, sourcePosition, destinationPosition);
                        model.getCommandManager().executeCommand(moveCommand);
                        update();
                    } catch (IOException e) {
                        alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Move Operation Failed", "An error occurred during the move operation: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Move Operation Failed", "Cannot move to the same position.");
                }
                gui.getGuiTreeView().setOnMouseClicked(null);
            }
        });
    }


    /**
     * Handles the removal of a document.
     */

    public void doRemove() {
        try {
            TreeItem<Document> selectedItem = getSelectedItem();
            Position<Document> selectedPosition = getSelectedPosition();
            previousSelection = selectedPosition;
            previousItem = selectedItem;
            RemoveCommand removeCommand = new RemoveCommand(model, selectedPosition);
            model.getCommandManager().executeCommand(removeCommand);
            TreeItem<Document> parent = selectedItem.getParent();

            if (parent != null) {
                parent.getChildren().remove(selectedItem);
                List<Document> documentsInFolder = model.getDirectDescendants(model.getPfs().parent(previousSelection), "all");

                addDocumentToGrid(documentsInFolder);
                updateDocumentGrid();
            } else {
                gui.getGuiTreeView().setRoot(null);
            }
        } catch (IOException e) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Remove Operation Failed", "An error occurred during the remove operation: " + e.getMessage());
        }
    }

    /**
     * Initiates the renaming of a document.
     */
    public void doRename() {
        RenameGUI renameGUI = new RenameGUI(model);
        new RenameController(model, renameGUI, getSelectedItem(), getSelectedPosition(), this);
    }

    /**
     * Initiates the editing of a document.
     */
    public void doEdit() throws IOException {

        if (getSelectedPosition().element().isAccessable()) {
            EditGUI editGUI = new EditGUI(model, getSelectedPosition());
            new EditController(model, editGUI, getSelectedItem(), getSelectedPosition());
            update();
        } else {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Editing not allowed", "The selected document cannot be edited.");
        }
    }

    /**
     * Initiates the zip operation on the selected document.
     */
    public void doZip() {
        try {
            Position<Document> selectedPosition = getSelectedPosition();
            if (selectedPosition.element().isAccessable()) {
                ZipCommand zipCommand = new ZipCommand(model, model.getZipper(), selectedPosition);
                model.getCommandManager().executeCommand(zipCommand);
                updateDocumentGrid();
            } else {
                alertUtils.showAlert(Alert.AlertType.WARNING, "Error", "Zip Operation Failed", "Cannot zip a zipped element");
            }
        } catch (IOException e) {
            alertUtils.showAlert(Alert.AlertType.WARNING, "Error", "Zip Operation Failed", "An error occurred during the zip operation: " + e.getMessage());
        }
    }

    public void doExtract() {
        try {
            Position<Document> selectedPosition = getSelectedPosition();
            if (!selectedPosition.element().isAccessable()) {
                ExtractCommand extractCommand = new ExtractCommand(model, model.getZipper(), selectedPosition);
                model.getCommandManager().executeCommand(extractCommand);
                updateDocumentGrid();
            } else {
                alertUtils.showAlert(Alert.AlertType.WARNING, "Error", "Zip Operation Failed", "Cannot extract something that is not zipped");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void doInfo() throws IOException {
        InfoGUI infoGUI = new InfoGUI(model);
        new InfoController(model, infoGUI);
    }

    public void doProperties() throws IOException {
        PropertiesGUI propGUI = new PropertiesGUI(model);
        new PropertiesController(model, propGUI);

    }

    /**
     * Undoes the last executed command.
     */
    public void doUndo() throws IOException {
        if (model.getCommandManager().getCommandLog().isEmpty()) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Extract Operation Failed", "Command stack is empty");
        } else {
            model.getCommandManager().undoCommand();
        }
        update();
    }

    /**
     * Redoes the last undone command.
     */
    public void doRedo() throws IOException {
        if (model.getCommandManager().getUndoneCommands().isEmpty()) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Extract Operation Failed", "Undone commands stack is empty");
        } else {
            model.getCommandManager().redoCommand();
        }
        update();
    }

    public void doBackUp() throws IOException {
        BackupCommand backupCommand = new BackupCommand(model);
        backupCommand.execute();
        alertUtils.showAlert(Alert.AlertType.INFORMATION, "Backup Operation", "Backup done", "Backup was successful and was stored in:  /src/main/java/pt/pa/Backup/Backups");
    }

    /**
     * Retrieves the currently selected item in the tree view.
     *
     * @return The selected tree item.
     */
    TreeItem<Document> getSelectedItem() {
        return gui.getGuiTreeView().getSelectionModel().getSelectedItem();
    }


    /**
     * Retrieves the position of the currently selected item in the tree view.
     *
     * @return The position of the selected item.
     */

    private Position<Document> getSelectedPosition() {
        return model.findPositionByElement(gui.getGuiTreeView().getSelectionModel().getSelectedItem().getValue());
    }

    /**
     * Creates the tree view based on the given tree structure.
     *
     * @param tree The tree structure representing the file system.
     */
    public void createTreeView(TreeLinked<Document> tree) {
        if (tree.root() != null) {
            TreeView<Document> treeView = gui.getGuiTreeView();
            TreeItem<Document> item = treeView.getRoot();
            Position<Document> rootPos = tree.root();
            recursiveCreateTreeView(tree, rootPos, item);
        }
    }

    private void recursiveCreateTreeView
            (TreeLinked<Document> tree, Position<Document> currentPosition, TreeItem<Document> parentItem) {
        for (Position<Document> child : tree.children(currentPosition)) {
            TreeItem<Document> childItem = new TreeItem<>(child.element());
            setItemImage(childItem);
            parentItem.getChildren().add(childItem);
            recursiveCreateTreeView(tree, child, childItem);
        }
    }


    /**
     * Handles the user's click on a tree view item.
     *
     * @param selectedItem The selected tree view item.
     */
    public void handleTreeViewItemClick(TreeItem<Document> selectedItem) {
        if (selectedItem != null) {

            Position<Document> selectedPosition = model.findPositionByElement(selectedItem.getValue());

            if (selectedPosition != null && selectedPosition.element() instanceof Folder) {
                List<Document> documentsInFolder = model.getDirectDescendants(selectedPosition, "all");
                addDocumentToGrid(documentsInFolder);
            }
        }
    }

    private void setItemImage(TreeItem<Document> item) {
        Document document = item.getValue();
        if (document instanceof MyFile) {
            item.setGraphic(new ImageView("file:src/main/java/pt/pa/View/Images/file1.png"));
        } else if (document instanceof Folder) {
            item.setGraphic(new ImageView("file:src/main/java/pt/pa/View/Images/folder1.png"));
        }
    }


    private void update() throws IOException {
        TreeItem<Document> selectedItem = gui.getGuiTreeView().getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            previousSelection = model.findPositionByElement(selectedItem.getValue());
        }
        TreeItem<Document> root = new TreeItem<>(model.getRoot().element());
        gui.getGuiTreeView().setRoot(root);
        createTreeView(model.getPfs());

        if (previousSelection != null) {
            TreeItem<Document> newSelectedItem = findTreeViewItem(previousSelection, gui.getGuiTreeView().getRoot());
            if (newSelectedItem != null) {
                gui.getGuiTreeView().getSelectionModel().select(newSelectedItem);
            }
        }
        if (previousSelection != null && previousSelection.element() instanceof Folder) {
            List<Document> documentsInFolder = model.getDirectDescendants(previousSelection, "all");
            addDocumentToGrid(documentsInFolder);
        }
        gui.getGuiTreeView().getSelectionModel().getSelectedItem().setExpanded(true);
        PFS.saveFileSystem(model.getPfs());
    }

    private TreeItem<Document> findTreeViewItem(Position<Document> target, TreeItem<Document> currentNode) {
        if (currentNode.getValue().equals(target.element())) {
            return currentNode;
        }

        for (TreeItem<Document> child : currentNode.getChildren()) {
            TreeItem<Document> result = findTreeViewItem(target, child);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * Updates the document grid based on the last set of documents.
     */
    public void updateDocumentGrid() throws IOException {
        documentGridManager.clearGrid();
        addDocumentToGrid(lastAddedDocuments);

        if (previousSelection != null) {
            TreeItem<Document> newSelectedItem = findTreeViewItem(previousSelection, previousItem);
            System.out.println(newSelectedItem);
            if (newSelectedItem != null) {
                gui.getGuiTreeView().getSelectionModel().select(newSelectedItem);
            }
        }
        PFS.saveFileSystem(model.getPfs());
    }


    /**
     * Adds documents to the document grid and updates the internal state.
     *
     * @param documents The list of documents to be added to the grid.
     */

    public void addDocumentToGrid(List<Document> documents) {
        documentGridManager.addDocumentsToGrid(documents);
        lastAddedDocuments = new ArrayList<>(documents);
    }
}