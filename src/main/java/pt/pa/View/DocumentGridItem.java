package pt.pa.View;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import pt.pa.Document;
import pt.pa.Folder;
import pt.pa.MyFile;
import pt.pa.adts.Position;

/**
 * Represents an item in a document grid.
 */
public class DocumentGridItem extends VBox {

    private final Position<Document> position;
    private final Document document;
    private boolean selected;

    /**
     * Constructs a DocumentGridItem with the specified position and document.
     *
     * @param position The position of the document.
     * @param document The document associated with this grid item.
     */
    public DocumentGridItem(Position<Document> position, Document document) {
        super();
        this.position = position;
        this.document = document;
        this.selected = false;
        initialize();
    }

    /**
     * Initializes the DocumentGridItem by setting alignment, spacing, and adding child nodes.
     */
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(5);

        Node documentNode = createDocumentNode(document);
        getChildren().add(documentNode);

        Label nameLabel = new Label(document.getName());
        nameLabel.setWrapText(true);
        getChildren().add(nameLabel);
    }

    /**
     * Creates a graphical representation (Node) for the given document.
     *
     * @param document The document for which to create a graphical representation.
     * @return A Node representing the document.
     */
    private Node createDocumentNode(Document document) {
        if (document instanceof MyFile myFile && myFile.isAccessable()) {
            String extension = myFile.getExtension().getExtension().toLowerCase();
            return createImageView("file:src/main/java/pt/pa/view/images/" + getIconNameForExtension(extension) + ".png");
        } else if (document instanceof Folder && document.isAccessable()) {
            return createImageView("file:src/main/java/pt/pa/view/images/folderFull.png");
        } else if (document instanceof Folder && !document.isAccessable()) {
            return createImageView("file:src/main/java/pt/pa/view/images/zipFolder.png");
        } else if (document instanceof MyFile myFile && !myFile.isAccessable()) {
            return createImageView("file:src/main/java/pt/pa/view/images/zipFile.png");
        } else {
            return new Label(document.getName());
        }
    }

    /**
     * Gets the icon name for the given file extension.
     *
     * @param extension The file extension.
     * @return The icon name corresponding to the extension.
     */
    private String getIconNameForExtension(String extension) {
        return switch (extension) {
            case ".doc" -> "doc";
            case ".pdf" -> "pdf";
            case ".jpg" -> "jpg";
            case ".png" -> "png";
            case ".html" -> "html";
            case ".xml" -> "xml";
            case ".csv" -> "csv";
            case ".zip" -> "zip";
            case ".mp3" -> "mp3";
            case ".mp4" -> "mp4";
            default -> "txt";
        };
    }

    /**
     * Creates an ImageView for the given image path with specified dimensions.
     *
     * @param imagePath The path to the image file.
     * @return An ImageView representing the image.
     */
    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView(imagePath);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        return imageView;
    }


    /**
     * Gets the position of the document in the grid.
     *
     * @return The position of the document.
     */
    public Position<Document> getPosition() {
        return position;
    }

    /**
     * Gets the document associated with this grid item.
     *
     * @return The document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Checks if the grid item is selected.
     *
     * @return True if selected, false otherwise.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selection state of the grid item.
     *
     * @param selected True to select, false to deselect.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        setStyle(selected ? "-fx-background-color: light blue;" : "-fx-background-color: none;");
    }
}
