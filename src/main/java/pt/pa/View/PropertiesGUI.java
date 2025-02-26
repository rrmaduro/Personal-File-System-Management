package pt.pa.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.Document;
import pt.pa.PFS;

/**
 * The PropertiesGUI class represents a graphical user interface for displaying properties of a file system.
 * It includes information about occupied space, the number of directories and files, depth, and top directories.
 * Users can refresh the information and close the properties window.
 */
public class PropertiesGUI extends VBox {
    private PFS model;
    private Stage primaryStage;
    private ListView listView;
    private Label labelSpace, labelDirectoriesFiles, labelDepth, labelTopDirectories;
    private Button refreshButton, closeButton;
    private VBox vbox;

    /**
     * Constructs a PropertiesGUI with the specified model.
     *
     * @param model The PFS model.
     */
    public PropertiesGUI(PFS model) {
        this.model = model;
        createPanel();
    }

    private void createPanel() {
        primaryStage = new Stage();
        initializeComponents();
        setupLayout();
        createAndShowScene();
    }

    private void initializeComponents() {
        listView = new ListView();
        labelSpace = new Label("Occupied space:");
        labelDirectoriesFiles = new Label("Directories: 0, Files: 0");
        labelDepth = new Label("Depth: 0");
        labelTopDirectories = new Label("Top 5 Directories:");
        refreshButton = new Button("Refresh");
        closeButton = new Button("Close");
    }

    private void setupLayout() {
        HBox buttonsHBox = createButtonsHBox();
        VBox.setMargin(buttonsHBox, new Insets(10, 5, 5, 0));

        vbox = new VBox();
        vbox.getChildren().addAll(labelSpace, labelDirectoriesFiles, labelDepth, labelTopDirectories, listView, buttonsHBox);
        VBox.setVgrow(buttonsHBox, Priority.ALWAYS);
    }

    private HBox createButtonsHBox() {
        HBox buttonsHBox = new HBox(5);
        buttonsHBox.getChildren().addAll(refreshButton, closeButton);
        buttonsHBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonsHBox;
    }

    private void createAndShowScene() {
        Scene scene = new Scene(vbox, 350, 450);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Properties");
        primaryStage.show();
    }

    /**
     * Retrieves the main VBox containing the components of the PropertiesGUI.
     *
     * @return The main VBox.
     */
    public VBox getVBox() {
        return vbox;
    }

    /**
     * Sets event triggers for the buttons in the PropertiesGUI.
     *
     * @param controller The controller managing the properties updates.
     */
    public void setTriggers(PropertiesController controller) {
        closeButton.setOnAction(event -> {
            controller.updateProperties();
            primaryStage.close();
        });

        refreshButton.setOnAction(event -> controller.updateProperties());
    }
}
