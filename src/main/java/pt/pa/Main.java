package pt.pa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.pa.View.GUI;
import pt.pa.View.GUIController;

import java.io.IOException;

/**
 * The Main class represents the main entry point for the Personal File System application.
 * It initializes the graphical user interface (GUI) and demonstrates the functionality
 * of the file system by creating folders, files, moving, renaming, and copying documents,
 * and displaying the file system structure using JavaFX components.
 */
public class Main extends Application {

    public Main() {
    }

    /**
     * The default entry point of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }


    //PFS pfs = new PFS(PFS.loadFileSystem());

    /**
     * Overrides the start method to initialize the GUI and demonstrate file system functionality.
     *
     * @param primaryStage The primary stage for the application.
     * @throws IOException If an I/O error occurs during the file system operations.
     */
    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        PFS pfs = new PFS(new Folder("root"));
        PFS especicPfs = new PFS(pfs.loadFileSystem());


      /*  pfs.populatePFS();
        GUI gui = new GUI(pfs);
        new GUIController(pfs, gui);*/


        GUI gui = new GUI(especicPfs);
        new GUIController(especicPfs, gui);


        Scene scene = new Scene(gui, 1280, 768);
        primaryStage.setTitle("Personal File System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}