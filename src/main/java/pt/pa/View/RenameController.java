package pt.pa.View;

import javafx.scene.control.TreeItem;
import pt.pa.Commands.RenameCommand;
import pt.pa.Document;
import pt.pa.Log.ProcedureLogger;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The RenameController class handles the interaction between the RenameGUI and the PFS (File System) model.
 * It allows renaming documents and communicates with the command manager to execute rename commands.
 */
public class RenameController {
    private final PFS model;
    private final RenameGUI renameGUI;
    private final TreeItem<Document> document;
    private GUIController guiController;
    private final Position<Document> selectedPosition;
    private ProcedureLogger procedureLogger = new ProcedureLogger();

    /**
     * Constructs a RenameController with the specified PFS model, RenameGUI, TreeItem representing a document,
     * and the corresponding Position in the file system.
     *
     * @param model            The PFS model representing the file system.
     * @param renameGUI        The RenameGUI for user interaction.
     * @param document         The TreeItem representing the document to be renamed.
     * @param selectedPosition The Position of the selected document in the file system.
     */
    public RenameController(PFS model, RenameGUI renameGUI, TreeItem<Document> document, Position<Document> selectedPosition, GUIController guiController) {
        this.model = model;
        this.renameGUI = renameGUI;
        this.document = document;
        this.selectedPosition = selectedPosition;
        this.guiController = guiController;
        configureGUI();
    }

    /**
     * Configures the RenameGUI by setting up event triggers for renaming operations.
     */
    private void configureGUI() {
        renameGUI.setTriggers(this);
    }

    /**
     * Saves the new name for the document and executes a RenameCommand through the command manager.
     *
     * @param newName The new name for the document.
     * @throws IOException If an I/O error occurs during the execution of the RenameCommand.
     */
    public void save(String newName) throws IOException {
        try {
            executeRenameCommand(newName);
            updateDocumentTreeItem();
            guiController.updateDocumentGrid();
        } catch (IOException e) {
            handleCommandExecutionError(e);
        }
    }

    /**
     * Executes the RenameCommand to update the document's name in the file system.
     *
     * @param newName The new name for the document.
     * @throws IOException If an I/O error occurs during the execution of the RenameCommand.
     */
    private void executeRenameCommand(String newName) throws IOException {
        RenameCommand renameCommand = new RenameCommand(model, selectedPosition, newName);
        model.getCommandManager().executeCommand(renameCommand);
    }

    /**
     * Updates the TreeItem representing the document in the GUI after a successful rename operation.
     */
    private void updateDocumentTreeItem() {
        document.setValue(null);
        document.setValue(selectedPosition.element());
    }

    /**
     * Handles errors that occur during the execution of the RenameCommand.
     *
     * @param e The IOException representing the error.
     * @throws IOException The same IOException is rethrown for further handling.
     */
    private void handleCommandExecutionError(IOException e) throws IOException {
        System.err.println("Error executing RenameCommand: " + e.getMessage());
        throw e;
    }
}
