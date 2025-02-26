package pt.pa.View;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.PFS;

import java.io.IOException;

/**
 * The RenameGUI class represents the graphical user interface for document renaming.
 */
public class RenameGUI extends VBox {

    private PFS model;
    private Stage primaryStage;
    private TextField textField;
    private Button save;
    private Button cancel;

    /**
     * Constructs a RenameGUI with the specified model.
     *
     * @param model The PFS model.
     */
    public RenameGUI(PFS model) {
        this.model = model;
        createPanel();
    }

    /**
     * Creates and displays the graphical user interface for document renaming.
     */
    public void createPanel() {
        primaryStage = new Stage();
        initTextField();
        initButtons();
        configureLayout();
        configureStageAndShow();
    }

    /**
     * Initializes the text field used for entering the new document name.
     */
    private void initTextField() {
        textField = new TextField();
        textField.setPromptText("Enter new name");
        textField.setMinWidth(250);
    }

    /**
     * Initializes the "Save" and "Cancel" buttons with their respective styles.
     */
    private void initButtons() {
        save = createStyledButton("Save", "#4CAF50");
        cancel = createStyledButton("Cancel", "#f44336");
    }

    /**
     * Creates a styled button with the specified text and background color.
     *
     * @param text            The text to be displayed on the button.
     * @param backgroundColor The background color of the button.
     * @return The styled button.
     */
    private Button createStyledButton(String text, String backgroundColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white;");
        return button;
    }

    /**
     * Configures the layout of the graphical user interface.
     */
    private void configureLayout() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(save, cancel);
        hbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(textField, hbox);

        Scene scene = new Scene(vbox, 300, 65);
        primaryStage.setTitle("Rename Document");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    /**
     * Configures the stage settings and shows the graphical user interface.
     */
    private void configureStageAndShow() {
        primaryStage.show();
    }

    /**
     * Sets event triggers for the buttons in the GUI.
     *
     * @param controller The RenameController to handle button actions.
     */
    public void setTriggers(RenameController controller) {
        save.setOnAction((ActionEvent event) -> {
            try {
                controller.save(textField.getText());
                textField.clear();
                primaryStage.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cancel.setOnAction((ActionEvent event) -> {
            primaryStage.close();
        });
    }
}