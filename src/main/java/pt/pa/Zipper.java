package pt.pa;

import pt.pa.adts.InvalidPositionException;
import pt.pa.adts.Position;

/**
 * The Zipper class provides methods to zip and unzip the content of a file system.
 * Zipping a folder makes its content inaccessible, while unzipping makes it accessible again.
 */
public class Zipper {

    private final PFS pfs;

    /**
     * Constructs a Zipper object with the specified file system.
     *
     * @param pfs The file system to operate on.
     */
    public Zipper(PFS pfs) {
        this.pfs = pfs;
    }

    /**
     * Zips the content of a folder, making it unable to perform any actions.
     *
     * @param folderPosition The position of the folder to be zipped.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    public void zipFolder(Position<Document> folderPosition) throws InvalidPositionException {
        validateFolderPosition(folderPosition);
        zipFolderContents(folderPosition);
        folderPosition.element().setAccess(false);
    }

    /**
     * Recursively zips the contents of a folder and makes all MyFile instances within it inaccessible.
     *
     * @param folderPosition The position of the folder whose contents are to be zipped.
     * @throws InvalidPositionException If the provided folder position is invalid.
     */
    private void zipFolderContents(Position<Document> folderPosition) throws InvalidPositionException {
        for (Position<Document> child : pfs.getPfs().children(folderPosition)) {
            if (child.element() instanceof MyFile myFile) {
                myFile.setAccess(false);
            } else if (child.element() instanceof Folder) {
                zipFolderContents(child);
            }
        }
    }

    /**
     * Zips an individual file, making it unable to perform any actions.
     *
     * @param filePosition The position of the file to be zipped.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    public void zipFile(Position<Document> filePosition) throws InvalidPositionException {
        validateFilePosition(filePosition);
        filePosition.element().setAccess(false);
    }

    /**
     * Unzips the content of a folder, making it accessible again.
     *
     * @param folderPosition The position of the folder to be unzipped.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    public void unzipFolder(Position<Document> folderPosition) throws InvalidPositionException {
        validateFolderPosition(folderPosition);
        unzipFolderContents(folderPosition);
        folderPosition.element().setAccess(true);
    }

    /**
     * Recursively unzips the contents of a folder and makes all MyFile instances within it accessible again.
     *
     * @param folderPosition The position of the folder whose contents are to be unzipped.
     * @throws InvalidPositionException If the provided folder position is invalid.
     */
    private void unzipFolderContents(Position<Document> folderPosition) throws InvalidPositionException {
        for (Position<Document> child : pfs.getPfs().children(folderPosition)) {
            if (child.element() instanceof MyFile myFile) {
                myFile.setAccess(true);
            } else if (child.element() instanceof Folder) {
                unzipFolderContents(child);
            }
        }
    }

    /**
     * Unzips an individual file, making it accessible again.
     *
     * @param filePosition The position of the file to be unzipped.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    public void unzipFile(Position<Document> filePosition) throws InvalidPositionException {
        validateFilePosition(filePosition);
        filePosition.element().setAccess(true);
    }

    /**
     * Validates whether the provided position represents a folder.
     *
     * @param folderPosition The position to be validated.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    private void validateFolderPosition(Position<Document> folderPosition) throws InvalidPositionException {
        if (folderPosition == null || !(folderPosition.element() instanceof Folder)) {
            throw new InvalidPositionException("Invalid folder position");
        }
    }

    /**
     * Validates whether the provided position represents a file.
     *
     * @param filePosition The position to be validated.
     * @throws InvalidPositionException If the provided position is invalid.
     */
    private void validateFilePosition(Position<Document> filePosition) throws InvalidPositionException {
        if (filePosition == null || !(filePosition.element() instanceof MyFile)) {
            throw new InvalidPositionException("Invalid file position");
        }
    }
}
