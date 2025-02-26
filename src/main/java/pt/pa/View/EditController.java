package pt.pa.View;

import javafx.scene.control.TreeItem;
import pt.pa.Commands.EditCommand;
import pt.pa.Document;
import pt.pa.Log.ProcedureLogger;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The EditController class is responsible for handling user interactions and managing
 * the editing of document content in the application. It connects the graphical user
 * interface (EditGUI) with the underlying file system model (PFS) and executes editing
 * commands based on user input.
 */
public class EditController {
    private PFS model;
    private EditGUI editGUI;
    private TreeItem<Document> document;
    private Position<Document> selectedPosition;


    /**
     * Constructs an EditController with the specified file system model, EditGUI,
     * document TreeItem, and selected position within the document tree.
     *
     * @param model              The file system model (PFS) providing access to documents.
     * @param editGUI            The graphical user interface for document editing.
     * @param document           The TreeItem representing the document being edited.
     * @param selectedPosition   The position of the document within the file system.
     */
    public EditController(PFS model, EditGUI editGUI, TreeItem document, Position<Document> selectedPosition) {
        this.model = model;
        this.editGUI = editGUI;
        this.document = document;
        this.selectedPosition = selectedPosition;
        editGUI.setTriggers(this);
    }

    /**
     * Initiates the process of editing the content of a document with the specified new content.
     * Executes an EditCommand on the file system model to perform the editing operation.
     *
     * @param newContent The new content to replace the existing content of the document.
     * @throws IOException If an error occurs during the execution of the EditCommand.
     */
    public void edit(String newContent) throws IOException {
        try {
            EditCommand editCommand = new EditCommand(model, selectedPosition, newContent);
            model.getCommandManager().executeCommand(editCommand);
        } catch (IOException e) {
            System.err.println("Error executing EditCommand: " + e.getMessage());
            throw e;
        }
    }
}
