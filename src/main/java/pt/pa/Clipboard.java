package pt.pa;

import pt.pa.adts.Position;

/**
 * The Clipboard class represents a clipboard that holds the content of a document position.
 */
public class Clipboard {
    private Position<Document> content;

    /**
     * Sets the content of the clipboard to the specified document position.
     *
     * @param content The document position to be set as the clipboard content.
     */
    public void setContent(Position<Document> content) {
        this.content = content;
    }

    /**
     * Gets the content of the clipboard.
     *
     * @return The document position stored in the clipboard.
     */
    public Position<Document> getContent() {
        return content;
    }

    /**
     * Clears the clipboard by setting its content to null.
     */
    public void clear() {
        this.content = null;
    }

    /**
     * Checks if the clipboard has content.
     *
     * @return true if the clipboard has content, false otherwise.
     */
    public boolean hasContent() {
        return content != null;
    }

    /**
     * Copies the content from another clipboard.
     *
     * @param otherClipboard The clipboard from which to copy content.
     */
    public void copyFrom(Clipboard otherClipboard) {
        this.content = otherClipboard.getContent();
    }

    /**
     * Checks if the clipboard content matches a given position.
     *
     * @param position The position to check against the clipboard content.
     * @return true if the clipboard content matches the given position, false otherwise.
     */
    public boolean contentMatches(Position<Document> position) {
        return content != null && content.equals(position);
    }
}
