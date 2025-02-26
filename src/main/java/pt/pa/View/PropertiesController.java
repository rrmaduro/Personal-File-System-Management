package pt.pa.View;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import pt.pa.Document;
import pt.pa.Folder;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The PropertiesController class is responsible for managing and updating properties information
 * displayed in the PropertiesGUI. It connects the graphical user interface with the underlying
 * file system model (PFS) and updates the displayed information based on user input or changes in the model.
 */
public class PropertiesController {
    private PFS model;
    private PropertiesGUI propertiesGUI;
    private VBox vbox;
    private int currentDepth;

    /**
     * Constructs a PropertiesController with the specified model and associated graphical user interface.
     *
     * @param model        The PFS model.
     * @param propertiesGUI The graphical user interface for displaying properties.
     */
    public PropertiesController(PFS model, PropertiesGUI propertiesGUI) {
        this.model = model;
        this.propertiesGUI = propertiesGUI;
        this.vbox = propertiesGUI.getVBox();
        propertiesGUI.setTriggers(this);
        updateProperties();
    }

    /**
     * Updates the displayed properties information.
     */
    public void updateProperties() {
        updateDirectoriesFilesCount();
        updateDepth();
        updateTopDirectories();
    }

    /**
     * Atualiza a contagem de diretórios e arquivos na interface gráfica.
     */
    private void updateDirectoriesFilesCount() {
        int[] directoriesFilesCount = countDirectoriesAndFiles(model.getRoot());
        Label labelDirectoriesFiles = (Label) vbox.getChildren().get(1);
        labelDirectoriesFiles.setText("Directories: " + directoriesFilesCount[0] + ", Files: " + directoriesFilesCount[1]);
    }

    /**
     * Atualiza a profundidade na interface gráfica.
     */
    private void updateDepth() {
        int depth = calculateDepth(model.getRoot());
        Label labelDepth = (Label) vbox.getChildren().get(2);
        labelDepth.setText("Depth: " + depth);
    }

    /**
     * Atualiza a lista dos top 5 diretórios na interface gráfica.
     */
    private void updateTopDirectories() {
        List<String> topDirectories = getTopDirectories(model.getRoot());
        Label labelTopDirectories = (Label) vbox.getChildren().get(3);
        labelTopDirectories.setText("Top 5 Directories: " + String.join(", ", topDirectories));
    }

    /**
     * Calculates the occupied space of a given directory.
     *
     * @param directory The position of the directory.
     * @return The occupied space of the directory.
     * @throws NullPointerException      if the provided directory position is null.
     * @throws IllegalArgumentException  if the provided position does not point to a folder.
     */
    public long calculateOccupiedSpace(Position<Document> directory) {
        if (directory == null) {
            throw new NullPointerException("Provided directory position is null.");
        }

        if (!(directory.element() instanceof Folder)) {
            throw new IllegalArgumentException("Provided position does not point to a folder.");
        }

        long occupiedSpace = 0;
        for (Position<Document> child : model.getPfs().children(directory)) {
            occupiedSpace += calculateOccupiedSpaceRecursive(child);
        }

        return occupiedSpace;
    }

    /**
     * Calculates the occupied space recursively starting from the given position.
     *
     * @param position The starting position for calculating occupied space.
     * @return The occupied space calculated from the given position.
     */
    private long calculateOccupiedSpaceRecursive(Position<Document> position) {
        if (position.element() instanceof MyFile myFile) {
            return model.calculateObjectSize(position);
        } else if (position.element() instanceof Folder) {
            return calculateOccupiedSpace(position);
        }

        return 0;
    }

    /**
     * Counts the number of directories and files in a given directory.
     *
     * @param position The position of the directory.
     * @return An array containing the count of directories at index 0 and files at index 1.
     */
    private int[] countDirectoriesAndFiles(Position<Document> position) {
        int[] count = {0, 0};
        countDirectoriesAndFilesRecursive(position, count);
        return count;
    }

    /**
     * Recursively counts the number of directories and files starting from the given position.
     *
     * @param position The starting position for counting directories and files.
     * @param count    The array to store the count, with directories at index 0 and files at index 1.
     */
    private void countDirectoriesAndFilesRecursive(Position<Document> position, int[] count) {
        if (position.element() instanceof Folder) {
            for (Position<Document> child : model.getPfs().children(position)) {
                if (child.element() instanceof Folder) {
                    count[0]++;
                    countDirectoriesAndFilesRecursive(child, count);
                } else if (child.element() instanceof MyFile) {
                    count[1]++;
                }
            }
        }
    }

    /**
     * Calculates the depth of a given position in the file system.
     *
     * @param pos The position for which to calculate the depth.
     * @return The depth of the position.
     * @throws NullPointerException if the provided position is null.
     */
    public int calculateDepth(Position<Document> pos) {
        if (pos == null) {
            throw new NullPointerException("Provided position is null.");
        }

        return calculateDepthRecursive(pos);
    }

    /**
     * Recursively calculates the depth starting from the given position.
     *
     * @param pos          The starting position for calculating the depth.
     * @return The depth calculated from the given position.
     */
    private int calculateDepthRecursive(Position<Document> pos) {
        if (model.getPfs().isRoot(pos)) {
            return 0;
        }

        Position<Document> parent = model.getPfs().parent(pos);
        return 1 + calculateDepthRecursive(parent);
    }

    /**
     * Represents the count of descendants for a directory, used for sorting.
     */
    private static class DirectoryDescendantsCount implements Comparable<DirectoryDescendantsCount> {
        private Position<Document> directory;
        private int descendantsCount;

        /**
         * Constructs a DirectoryDescendantsCount object with the specified directory and descendants count.
         *
         * @param directory        The position of the directory.
         * @param descendantsCount The count of descendants for the directory.
         */
        public DirectoryDescendantsCount(Position<Document> directory, int descendantsCount) {
            this.directory = directory;
            this.descendantsCount = descendantsCount;
        }

        /**
         * Gets the position of the directory.
         *
         * @return The position of the directory.
         */
        public Position<Document> getDirectory() {
            return directory;
        }

        @Override
        public int compareTo(DirectoryDescendantsCount other) {
            return Integer.compare(this.descendantsCount, other.descendantsCount);
        }
    }

    /**
     * Gets the top 5 directories based on the count of descendants.
     *
     * @param root The root position of the file system.
     * @return A list of names of the top 5 directories.
     */
    public List<String> getTopDirectories(Position<Document> root) {
        List<DirectoryDescendantsCount> directoryCounts = new ArrayList<>();
        calculateDescendantsCount(root, directoryCounts);

        directoryCounts.sort(Collections.reverseOrder());

        List<Position<Document>> topDirectories = directoryCounts.stream()
                .limit(5)
                .map(DirectoryDescendantsCount::getDirectory)
                .collect(Collectors.toList());

        return topDirectories.stream()
                .map(directory -> directory.element().getName())
                .collect(Collectors.toList());
    }

    /**
     * Recursively calculates the descendants count for each directory starting from the given position.
     *
     * @param directory       The starting position for calculating descendants count.
     * @param directoryCounts The list to store DirectoryDescendantsCount objects.
     * @return The total descendants count from the given directory.
     */
    private int calculateDescendantsCount(Position<Document> directory, List<DirectoryDescendantsCount> directoryCounts) {
        int descendantsCount = 0;
        for (Position<Document> child : model.getPfs().children(directory)) {
            descendantsCount += calculateDescendantsCount(child, directoryCounts);
        }

        if (directory.element() instanceof Folder) {
            directoryCounts.add(new DirectoryDescendantsCount(directory, descendantsCount));
        }

        return descendantsCount + 1;
    }
}