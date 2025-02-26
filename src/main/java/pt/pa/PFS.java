package pt.pa;

import pt.pa.Backup.Memento;
import pt.pa.Backup.Originator;
import pt.pa.Commands.CommandManager;
import pt.pa.Exceptions.InvalidMoveException;
import pt.pa.Exceptions.InvalidNameException;
import pt.pa.Factories.FolderFactory;
import pt.pa.Factories.MyFileFactory;
import pt.pa.adts.InvalidPositionException;
import pt.pa.adts.Position;
import pt.pa.adts.TreeLinked;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The PFS (File System) class represents a hierarchical file system using a tree data structure.
 * It allows creating, managing, and manipulating documents, which can be files or folders.
 * Documents are organized in a tree structure, where each node in the tree represents a document.
 */
public class PFS implements Originator {
    private TreeLinked<Document> pfs;
    private List<Document> allDocuments;
    private FolderFactory folderFactory = new FolderFactory();
    private MyFileFactory fileFactory = new MyFileFactory();
    private final CommandManager commandManager = new CommandManager();
    private final Clipboard clipboard = new Clipboard();
    private Zipper zipper;


    /**
     * Constructs a PFS instance with the specified root document.
     *
     * @param root The root document of the file system.
     */
    public PFS(Document root) throws IOException {
        this.pfs = new TreeLinked<>(root);
        this.zipper = new Zipper(this);
    }

    public PFS(TreeLinked<Document> tree) {
        this.pfs = tree;
        this.zipper = new Zipper(this);
    }

    /**
     * Gets the underlying tree structure representing the file system.
     *
     * @return The tree structure representing the file system.
     */
    public TreeLinked<Document> getPfs() {
        return pfs;
    }

    /**
     * Finds and returns the position of a document with the specified name in the file system.
     *
     * @param name The name of the document to find.
     * @return The position of the document with the specified name, or null if not found.
     * @throws InvalidNameException If the specified name is null.
     */
    public Position<Document> find(String name) {
        return findPositionByName(name, pfs.root());
    }

    /**
     * Finds and returns the position of a document with the specified name in the file system.
     * <p>
     * This method recursively searches for a document with the specified name starting from the provided
     * current position in the file system tree. If the specified name is null, an InvalidNameException is thrown.
     * The search is performed recursively in a depth-first manner, exploring child positions of the current
     * position until a matching document is found or the entire tree is searched. If a document with the specified
     * name is found, its position is returned; otherwise, null is returned.
     * </p>
     *
     * @param name            The name of the document to find.
     * @param currentPosition The current position in the file system tree to start the search.
     * @return The position of the document with the specified name, or null if not found.
     * @throws InvalidNameException If the specified name is null.
     * @see Folder
     * @see MyFile
     */
    private Position<Document> findPositionByName(String name, Position<Document> currentPosition)
            throws InvalidNameException {
        if (name == null) {
            throw new InvalidNameException("Invalid Name");
        }
        if (currentPosition.element().getName().equals(name)) {
            return currentPosition;
        }

        for (Position<Document> child : pfs.children(currentPosition)) {
            Position<Document> result = findPositionByName(name, child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Finds the position of a specific document within the file system.
     *
     * @param document The document to find within the file system.
     * @return The position of the document if found, or null if the document is null or not present.
     */
    public Position<Document> findPositionByElement(Document document) {
        if (document == null) {
            return null;
        }
        return findPositionByElementHelper(document, pfs.root());
    }

    /**
     * Helper method for recursively searching for the position of a document within the file system.
     *
     * @param target          The document to find.
     * @param currentPosition The current position being checked.
     * @return The position of the document if found, or null if not found.
     */
    private Position<Document> findPositionByElementHelper(Document target, Position<Document> currentPosition) {
        if (currentPosition.element().equals(target)) {
            return currentPosition;
        }
        for (Position<Document> child : pfs.children(currentPosition)) {
            Position<Document> result = findPositionByElementHelper(target, child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Inserts a document into the file system under a specified parent position.
     *
     * @param parent   The parent position under which the document will be inserted.
     * @param document The document to be inserted.
     * @return The position of the inserted document in the file system.
     * @throws IllegalArgumentException If the document type is unsupported.
     */
    public Position<Document> insertDocument(Position<Document> parent, Document document) {
        if (document instanceof MyFile file) {
            return pfs.insert(parent, fileFactory.create(file.getName()));
        } else if (document instanceof Folder folder) {
            return pfs.insert(parent, folderFactory.create(folder.getName()));
        } else {
            throw new IllegalArgumentException("Unsupported document type: " + document.getClass());
        }
    }

    /**
     * Inserts a document into the file system under a specified parent position.
     *
     * @param parent   The parent position under which the document will be inserted.
     * @param document The document to be inserted.
     * @return The position of the inserted document in the file system.
     */
    public Position<Document> insertDoc(Position<Document> parent, Document document) {
        return pfs.insert(parent, document);
    }


    /**
     * Creates a new file with the given name and inserts it as a child of the specified parent position.
     *
     * @param name   The name of the new file.
     * @param parent The position of the parent folder.
     * @return The position of the newly created file.
     */
    public Position<Document> createFile(String name, Position<Document> parent) {
        return pfs.insert(parent, fileFactory.create(name));
    }

    /**
     * Creates a new file with the given name and inserts it as a child of the specified parent position.
     *
     * @param name   The name of the new file.
     * @param parent The position of the parent folder.
     * @return The position of the newly created file.
     */
    public Position<Document> createFile(String name, String extension, Position<Document> parent) {
        return pfs.insert(parent, fileFactory.create(name, extension));
    }

    /**
     * Creates a new folder with the given name and inserts it as a child of the specified parent position.
     *
     * @param name   The name of the new folder.
     * @param parent The position of the parent folder.
     * @return The position of the newly created folder.
     */
    public Position<Document> createFolder(String name, Position<Document> parent) {
        return pfs.insert(parent, folderFactory.create(name));
    }


    /**
     * Renames the document at the specified position to the given new name.
     *
     * @param document The position of the document to be renamed.
     * @param newName  The new name for the document.
     * @throws InvalidPositionException If the specified position is invalid.
     * @throws InvalidNameException     If the specified new name is null.
     */
    public void rename(Position<Document> document, String newName)
            throws InvalidPositionException, InvalidNameException {
        try {
            Document doc = document.element();
            String oldName = doc.getName();
            doc.rename(newName);
            System.out.println("The " + (doc instanceof MyFile ? "file" : "folder") +
                    " '" + oldName + "' has been renamed to '" + newName + "'.");
        } catch (InvalidPositionException | InvalidNameException e) {
            throw new InvalidPositionException("Invalid Position");
        }
    }

    /**
     * Removes the document at the specified position from the file system.
     *
     * @param document The position of the document to be removed.
     * @throws InvalidPositionException If the specified position is invalid.
     */
    public void remove(Position<Document> document)
            throws InvalidPositionException {
        if (document == null) {
            throw new InvalidPositionException("Invalid Position");
        }
        pfs.remove(document);
        pfs.parent(document);

    }

    /**
     * Edits the content of a document at the specified position in the file system.
     *
     * @param document   The position of the document to be edited.
     * @param newContent The new content for the document.
     * @throws NullPointerException If the provided document position is null.
     */
    public void edit(Position<Document> document, String newContent) {
        if (document == null) {
            throw new NullPointerException("Provided document position is null.");
        }
        if (document.element().isAccessable()) {
            Document doc = document.element();
            if (doc instanceof MyFile myFile) {
                myFile.setContent(newContent);
                System.out.println("Content of '" + myFile.getName() + "' has been updated.");
            } else {
                System.out.println("Cannot edit. Not a file.");
            }
        } else {
            throw new IllegalStateException("Impossible to edit zipped files");
        }
    }

    /**
     * Displays the content of a file at the specified position.
     *
     * @param document The position of the file to display.
     * @throws NullPointerException If the provided document position is null.
     */
    public void show(Position<Document> document) {
        if (document == null) {
            throw new NullPointerException("Provided document position is null.");
        }

        Document doc = document.element();
        if (doc instanceof MyFile) {
            MyFile myFile = (MyFile) doc;
            String content = myFile.getContent();
            System.out.println("File Content:\n" + content);
        } else {
            System.out.println("Cannot show content. Not a file.");
        }
    }

    /**
     * Copies a document at the specified position to the clipboard.
     *
     * @param document The position of the document to be copied.
     * @return The position of the copied document in the clipboard.
     * @throws InvalidPositionException If the provided position is invalid (null).
     */
    public Position<Document> copy(Position<Document> document) {
        if (document == null) {
            throw new InvalidPositionException("Position is invalid.");
        } else {
            clipboard.setContent(document);
        }
        return clipboard.getContent();
    }

    /**
     * Pastes the document from the clipboard to the specified parent position.
     * <p>
     * Note: This method does not allow pasting inside files.
     *
     * @param parent The parent position where the document will be pasted.
     * @return The position of the pasted document in the file system.
     * @throws InvalidPositionException If the parent position is inside a MyFile.
     */
    public Position<Document> paste(Position<Document> parent) {
        if (parent.element() instanceof MyFile) {
            throw new InvalidPositionException("Cannot paste inside files");
        }
        return paste(parent, clipboard.getContent());
    }

    /**
     * Pastes the document from a specified source position to the specified parent position.
     *
     * @param parentPosition   The parent position where the document will be pasted.
     * @param documentPosition The source position of the document to be pasted.
     * @return The position of the pasted document in the file system.
     * @throws InvalidPositionException If the parent position is invalid, or if the document position is invalid
     *                                  or if the document position is an ancestor of the parent position.
     */
    public Position<Document> paste(Position<Document> parentPosition, Position<Document> documentPosition) {
        if (parentPosition != null && documentPosition != null && !pfs.isAncestor(documentPosition, parentPosition)) {
            Position<Document> newPosition = null;

            if (documentPosition.element() instanceof MyFile) {
                newPosition = insertDoc(parentPosition, fileFactory.createCopy((MyFile) documentPosition.element()));
            } else if (documentPosition.element() instanceof Folder) {
                newPosition = insertDoc(parentPosition, folderFactory.createCopy((Folder) documentPosition.element()));

                for (Position<Document> child : pfs.children(documentPosition)) {
                    paste(newPosition, child);
                }
            }

            return newPosition;
        }

        throw new InvalidPositionException("Invalid position");
    }


    /**
     * Moves a document from the initial position to the end position in the file system.
     *
     * @param initial The position of the document to be moved.
     * @param end     The position where the document will be moved.
     * @throws InvalidMoveException If the move operation is invalid.
     */
    public void move(Position<Document> initial, Position<Document> end) throws InvalidMoveException {
        if (end.element() instanceof Folder) {
            if (initial == null || end == null) {
                throw new InvalidMoveException("Invalid move operation: Positions cannot be null.");
            }
            pfs.move(initial, end);
        } else {
            throw new InvalidMoveException(" Invalid end position");
        }
    }

    /**
     * Returns a string representation of the file system.
     *
     * @return A string representation of the file system.
     */
    @Override
    public String toString() {
        return pfs.toString();
    }

    /**
     * Saves the current state of the PFS (File System) by creating a memento.
     *
     * @return A Memento representing the current state of the PFS.
     */
    @Override
    public Memento saveState() {
        return new PFSMemento(this.getPfs());
    }

    /**
     * Restores the state of the PFS from a saved Memento.
     *
     * @param savedMemento The Memento object representing a previous state of the PFS.
     * @throws IllegalArgumentException If the provided Memento is not of the expected type.
     */
    @Override
    public void setState(Memento savedMemento) {
        if (savedMemento instanceof PFSMemento) {
            pfs = ((PFSMemento) savedMemento).getPfsCopy();
        } else {
            throw new IllegalArgumentException("Unsupported error");
        }
    }

    /**
     * Saves the file system represented by the provided TreeLinked structure to a file.
     *
     * @param explorer The TreeLinked structure representing the file system to be saved.
     * @throws IOException If an I/O error occurs during the saving process.
     */
    public static void saveFileSystem(TreeLinked<Document> explorer) throws IOException {
        String fileName = "Explorer.pfs";
        FileOutputStream fin = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fin);
        oos.writeObject(explorer);
        oos.close();
    }

    /**
     * Loads a file system from a saved file and returns it as a TreeLinked structure.
     *
     * @return The TreeLinked structure representing the loaded file system.
     * @throws IOException            If an I/O error occurs during the loading process.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public TreeLinked<Document> loadFileSystem() throws IOException, ClassNotFoundException {
        String fileName = "Explorer.pfs";
        FileInputStream fin = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        TreeLinked<Document> tree = (TreeLinked<Document>) ois.readObject();
        ois.close();
        return tree;
    }

    /**
     * Retrieves the CommandManager associated with this file system.
     *
     * @return The CommandManager instance associated with this file system.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the direct descendants of a directory based on file type.
     *
     * @param directory The position of the directory.
     * @param fileType  The file type ("file", "folder", or "all").
     * @return A list of direct descendants based on the specified file type.
     * @throws InvalidPositionException If the specified position is invalid.
     */
    public List<Document> getDirectDescendants(Position<Document> directory, String fileType)
            throws InvalidPositionException {
        if (directory == null || directory.element() == null || !(directory.element() instanceof Folder)) {
            throw new InvalidPositionException("Invalid directory position");
        }

        List<Document> descendants = new ArrayList<>();

        for (Position<Document> child : pfs.children(directory)) {
            Document childDocument = child.element();

            if ("file".equalsIgnoreCase(fileType) && childDocument instanceof MyFile) {
                descendants.add(childDocument);
            } else if ("folder".equalsIgnoreCase(fileType) && childDocument instanceof Folder) {
                descendants.add(childDocument);
            } else if ("all".equalsIgnoreCase(fileType)) {
                descendants.add(childDocument);
            }
        }
        return descendants;
    }

    /**
     * Retrieves the root position of the file system.
     *
     * @return The position representing the root of the file system.
     */
    public Position<Document> getRoot() {
        return this.pfs.root();
    }

    /**
     * Retrieves the Clipboard associated with this file system.
     *
     * @return The Clipboard instance associated with this file system.
     */
    public Clipboard getClipboard() {
        return this.clipboard;
    }

    /**
     * Checks if a document has a child with the specified name in the file system.
     *
     * @param folder   The position of the folder to check for children.
     * @param document The document to check for as a child.
     * @return true if the folder has a child with the specified document, false otherwise.
     * @throws InvalidPositionException If the specified position is invalid.
     */
    public boolean hasChild(Position<Document> folder, Document document) throws InvalidPositionException {
        if (folder == null || folder.element() == null || !(folder.element() instanceof Folder)) {
            throw new InvalidPositionException("Invalid folder position");
        }

        for (Position<Document> child : pfs.children(folder)) {
            if (child.element().equals(document)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the size of a folder based on the size of all its files.
     *
     * @param pos The position of the folder in the file system.
     * @return The size of the folder in bytes.
     * @throws NullPointerException If the provided folder position is null.
     */
    public long calculateObjectSize(Position<Document> pos) {
        if (pos == null) {
            throw new NullPointerException("Provided folder position is null.");
        }
        if (pos instanceof MyFile myFile) {
            String content = myFile.getContent();
            return content.getBytes(StandardCharsets.UTF_8).length;
        } else if (pos.element() instanceof Folder) {

            long folderSize = 0;

            for (Position<Document> child : pfs.children(pos)) {
                if (child.element() instanceof MyFile myFile) {
                    folderSize += myFile.getContent().getBytes(StandardCharsets.UTF_8).length;
                }
            }

            return folderSize;
        }

        throw new IllegalArgumentException("Provided position does not point to a folder.");
    }

    /**
     * Returns the Zipper object associated with this file system.
     *
     * @return The Zipper object used for zip operations.
     */
    public Zipper getZipper() {
        return zipper;
    }



    /**
     * Gets the last 20 files or directories created, sorted in descending order by creation date.
     *
     * @return A list of the last 20 files or directories created.
     */
    public List<Document> getLast20CreatedFiles() {
        List<Document> allDocuments = new ArrayList<>();

        for (Position<Document> position : pfs.positions()) {
            allDocuments.add(position.element());
        }
        allDocuments.sort(Comparator.comparing(Document::getCreationDate).reversed());

        return allDocuments.stream().limit(20).collect(Collectors.toList());
    }

    /**
     * Gets the last 10 files altered, sorted in descending order by the date of creation.
     *
     * @return A list of the last 10 files altered.
     */
    public List<MyFile> getLast10Altered() {
        List<MyFile> files = new ArrayList<>();
        for (Position<Document> position : pfs.positions()) {
            Document document = position.element();
            if (document instanceof MyFile) {
                files.add((MyFile) document);
            }
        }

        files.sort(Comparator.comparing(MyFile::getLastChangeDate).reversed());

        return files.subList(0, Math.min(10, files.size()));
    }

    /**
     * Retrieves a list of documents filtered by the specified selected year.
     *
     * @param selectedYear The target year for filtering documents.
     * @return A list of documents that match the selected year.
     */
    public List<Document> getDocumentsBySelectedYear(int selectedYear) {
        return allDocuments.stream()
                .filter(document -> isYearMatch(document, selectedYear))
                .collect(Collectors.toList());
    }

    /**
     * Obtains data about files created per month in a specific year.
     *
     * @param year The year for which you want to obtain the data.
     * @return A Map where the key is the month and the value is the number of created files.
     */
    public Map<String, Integer> getCreatedFilesDataByMonth(int year) {
        Map<String, Integer> createdFilesData = new HashMap<>();

        for (Document document : allDocuments) {
            if (document instanceof MyFile && isYearMatch(document, year)) {
                LocalDateTime creationDateTime = unixToLocalDateTime(document.getCreationDate());
                String month = creationDateTime.getMonth().toString();

                createdFilesData.merge(month, 1, Integer::sum);
            }
        }

        return createdFilesData;
    }

    /**
     * Obtains data about files changed per month in a specific year.
     *
     * @param year The year for which you want to obtain the data.
     * @return A Map where the key is the month and the value is the number of changed files.
     */
    public Map<String, Integer> getChangedFilesDataByMonth(int year) {
        Map<String, Integer> changedFilesData = new HashMap<>();

        for (Document document : allDocuments) {
            if (document instanceof MyFile && isYearMatch(document, year)) {
                MyFile file = (MyFile) document;
                LocalDateTime lastChangeDateTime = unixToLocalDateTime(file.getLastChangeDate());
                String month = lastChangeDateTime.getMonth().toString();

                changedFilesData.merge(month, 1, Integer::sum);
            }
        }

        return changedFilesData;
    }

    /**
     * Checks if the creation year of the document matches the specified year.
     *
     * @param document The document to check.
     * @param year     The year to compare.
     * @return True if the creation year matches, false otherwise.
     */
    private boolean isYearMatch(Document document, int year) {
        LocalDateTime dateTime = unixToLocalDateTime(document.getCreationDate());
        return dateTime.getYear() == year;
    }

    /**
     * Converts a Unix timestamp to a LocalDateTime object.
     *
     * @param timestamp The Unix timestamp to convert.
     * @return The LocalDateTime object representing the timestamp.
     */
    private LocalDateTime unixToLocalDateTime(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }


    public void populatePFS() {
        createFolder("Downloads", find("root"));
        createFolder("Music", find("root"));
        createFolder("Pictures", find("root"));
        createFolder("Camera", find("Pictures"));
        createFolder("Prints", find("Pictures"));
        createFolder("Video", find("root"));
        createFolder("Documents", find("root"));
        createFolder("Github", find("Documents"));
        createFolder("projeto-en-as_202200637", find("Github"));
        createFolder("tutorial-2-202200252", find("Github"));

        createFile("bellsoft-jdk17.0.9+11-windows-amd64-full", ".txt", find("Downloads"));
        createFile("Relatorio_tecnico-Fase1", ".pdf", find("Downloads"));
        createFile("Juice WRLD - Lucid Dreams", ".mp3", find("Music"));
        createFile("Don Toliver - No Idea", ".mp3", find("Music"));
        createFile("Travis Scott - goosebumps", ".mp3", find("Music"));
        createFile("Toy - Toda a noite", ".mp3", find("Music"));
        createFile("family", ".jpg", find("Camera"));
        createFile("print1", ".png", find("root"));
        createFile("print2", ".png", find("root"));
        createFile("ferias-amesterdao", ".mp4", find("Video"));
        createFile("ferias-paris", ".mp4", find("Video"));
        createFile("PA2324_ProjetoEpocaNormal", ".doc", find("Documents"));
        createFile("Customer", ".csv", find("Documents"));

        move(find("print1"), find("Prints"));
        move(find("print2"), find("Prints"));

        edit(find("Customer"),
                "Miguel Pinto - 1x ChapÃ©u \nRodrigo Maduro - 1x SweatShirt \n" +
                        "Ricardo Pinto - 1x T-Shirt | 2x Meias \nMiguel Borges - 1x Laptop");
        show(find("Customer"));
        rename(find("Customer"), "ComprasClientes");
    }

    /**
     * The PFSMemento class represents a Memento for the PFS (File System).
     * It stores a copy of the tree structure representing the file system.
     */
    private static class PFSMemento implements Memento, Serializable {
        private TreeLinked<Document> pfsCopy;


        /**
         * Constructs a PFSMemento with a copy of the file system tree.
         *
         * @param pfsCopy The copied tree structure representing the file system.
         */
        public PFSMemento(TreeLinked<Document> pfsCopy) {
            this.pfsCopy = pfsCopy;
        }

        /**
         * Gets the copied tree structure representing the file system.
         *
         * @return The copied tree structure representing the file system.
         */
        public TreeLinked<Document> getPfsCopy() {
            return this.pfsCopy;
        }
    }
}
