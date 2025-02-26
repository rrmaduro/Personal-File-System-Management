package pt.pa.View;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pt.pa.Document;
import pt.pa.Folder;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.util.List;

/**
 * The DocumentGridManager class is responsible for managing the display of documents
 * in a JavaFX GridPane. It provides methods to add a list of documents to the GridPane,
 * creating visual representations for each document.
 */
public class DocumentGridManager {
    private final GridPane documentGrid;
    private PFS model;
    private GUI gui;


    /**
     * Constructs a DocumentGridManager with the specified GridPane.
     *
     * @param documentGrid The GridPane where documents will be displayed.
     */
    public DocumentGridManager(GridPane documentGrid, PFS model, GUI gui) {
        this.documentGrid = documentGrid;
        this.model = model;
        this.gui = gui;
    }

    /**
     * Adds a list of documents to the GridPane, creating visual representations for each document.
     * Clears the existing children in the GridPane before adding new documents.
     *
     * @param documents The list of documents to be added to the GridPane.
     */
    public void addDocumentsToGrid(List<Document> documents) {
        documentGrid.getChildren().clear();

        for (int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            Position<Document> position = model.findPositionByElement(document);

            DocumentGridItem documentItem = getDocumentGridItem(position, document);

            documentGrid.add(documentItem, i % 7, i / 7);
        }

        documentGrid.layout();
    }

    /**
     * Creates a {@link DocumentGridItem} with the specified position and document, and sets
     * the event handler for mouse clicks to handle single and double clicks.
     *
     * @param position The position of the document.
     * @param document The document to be represented by the grid item.
     * @return The created {@link DocumentGridItem}.
     */
    private DocumentGridItem getDocumentGridItem(Position<Document> position, Document document) {
        DocumentGridItem documentItem = new DocumentGridItem(position, document);

        documentItem.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 1) {
                    handleSingleClickSelection(documentItem);
                } else if (event.getClickCount() == 2) {
                    TreeItem<Document> clickedItem = convertDocumentGridItemToTreeItem(documentItem);
                    handleTreeGridItemDoubleClick(clickedItem);
                }
            }
        });

        return documentItem;
    }

    /**
     * Handles the single-click selection of a {@link DocumentGridItem}.
     *
     * @param clickedItem The clicked {@link DocumentGridItem}.
     */
    private void handleSingleClickSelection(DocumentGridItem clickedItem) {
        clearSelection();
        if (clickedItem.isSelected()) {
            clickedItem.setSelected(false);
        } else {
            clickedItem.setSelected(true);
            gui.getGuiTreeView().getSelectionModel().clearSelection();
        }
    }

    /**
     * Handles the double-click action on a {@link TreeItem<Document>}.
     * If the selected item represents a folder, it retrieves its direct descendants
     * and adds them to the grid.
     *
     * @param selectedItem The double-clicked {@link TreeItem<Document>}.
     */
    public void handleTreeGridItemDoubleClick(TreeItem<Document> selectedItem) {
        if (selectedItem != null) {
            Position<Document> selectedPosition = model.findPositionByElement(selectedItem.getValue());

            if (selectedPosition != null && selectedPosition.element() instanceof Folder) {
                List<Document> documentsInFolder = model.getDirectDescendants(selectedPosition, "all");
                addDocumentsToGrid(documentsInFolder);
            }
        }
    }

    /**
     * Converts a {@link DocumentGridItem} to a {@link TreeItem<Document>} for use in a tree view.
     * If the provided {@code documentGridItem} or its associated document is null, returns null.
     *
     * @param documentGridItem The {@link DocumentGridItem} to convert to a {@link TreeItem<Document>}.
     * @return The converted {@link TreeItem<Document>}, or null if the input is invalid.
     */
    public TreeItem<Document> convertDocumentGridItemToTreeItem(DocumentGridItem documentGridItem) {
        if (documentGridItem == null || documentGridItem.getDocument() == null) {
            return null;
        }

        Document document = documentGridItem.getDocument();

        TreeItem<Document> treeItem = new TreeItem<>(document);
        treeItem.setValue(document);
        treeItem.setExpanded(true);
        return treeItem;
    }

    /**
     * Clears the selection of all {@link DocumentGridItem}s within the associated {@link GridPane}.
     */
    public void clearSelection() {
        for (Node node : documentGrid.getChildren()) {
            if (node instanceof DocumentGridItem) {
                ((DocumentGridItem) node).setSelected(false);
            }
        }
    }

    /**
     * Clears all children from the associated {@link GridPane}.
     */
    public void clearGrid() {
        documentGrid.getChildren().clear();
    }

}
