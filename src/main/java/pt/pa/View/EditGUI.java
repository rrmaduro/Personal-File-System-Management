package pt.pa.View;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.Document;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The EditGUI class represents the graphical user interface for text editing.
 */
public class EditGUI extends VBox {
    private PFS model;
    private Stage primaryStage;
    private TextArea textArea;
    private Button edit;
    private Button cancel;
    private Position<Document> document;

    /**
     * Constructs an EditGUI with the specified model.
     *
     * @param model The PFS model.
     */
    public EditGUI(PFS model, Position<Document> document) {
        this.model = model;
        this.document= document;
        createPanel();
    }

    private void createPanel() {
        primaryStage = new Stage();

        textArea = new TextArea(((MyFile) (document.element())).getContent());
        textArea.setPrefColumnCount(20);

        edit = new Button("Save");
        cancel = new Button("Cancel");

        edit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        setSpacing(10);
        setAlignment(Pos.CENTER);

        getChildren().addAll(textArea, edit, cancel);

        Scene scene = new Scene(this, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Edit Text");

        primaryStage.show();
    }

    /**
     * Sets event triggers for the buttons in the GUI.
     *
     * @param controller The EditController to handle button actions.
     */
    public void setTriggers(EditController controller) {
        edit.setOnAction((ActionEvent event) -> {
            try {
                controller.edit(textArea.getText());
                textArea.clear();
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
