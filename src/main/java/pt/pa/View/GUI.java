package pt.pa.View;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import pt.pa.Document;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

public class GUI extends BorderPane {
    private TreeView<Document> guiTreeView;
    private PFS model;
    private DocumentGridManager documentGridManager;
    private GridPane documentGrid;
    private TextArea documentContent;
    private TreeItem<Document> root;
    private TextField searchBar;
    private Button createFolderBtn;
    private Button createFileBtn;
    private Button copyBtn;
    private Button pasteBtn;

    private Button moveBtn;
    private Button removeBtn;
    private Button editBtn;
    private Button renameBtn;
    private Button undoBtn;
    private Button redoBtn;
    private Button zipBtn;
    private Button extractBtn;
    private Button infoBtn;
    private Button propertiesBtn;
    private Button backupBtn;

    public GUI(PFS model) {
        this.model = model;
        createPanel();
        documentGridManager = new DocumentGridManager(documentGrid,model,this);
    }

    public TreeView<Document> getGuiTreeView() {
        return guiTreeView;
    }

    public GridPane getDocumentGrid() {
        return documentGrid;
    }
    private void createPanel() {
        initializeButtons();
        initializeSearchBar();
        initializeToolbars();
        initializeTreeView();
        initializeDocumentGrid();
        initializeDocumentContent();
    }

    private void initializeButtons() {
        createFolderBtn = new Button("New Folder");
        createFileBtn = new Button("New File");
        copyBtn = new Button("Copy");
        pasteBtn = new Button("Paste");
        moveBtn = new Button("Move");
        removeBtn = new Button("Remove");
        editBtn = new Button("Edit");
        renameBtn = new Button("Rename");
        undoBtn = new Button("Undo");
        redoBtn = new Button("Redo");
        zipBtn = new Button("Zip");
        extractBtn = new Button("Extract");
        infoBtn = new Button("Info");
        propertiesBtn = new Button("Properties");
        backupBtn = new Button("Backup");
    }

    private void initializeSearchBar() {
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setMaxWidth(300);
    }

    private void initializeToolbars() {
        ToggleButton toggleToolbar2Btn = new ToggleButton("More");

        ToolBar toolbar2 = new ToolBar(moveBtn, copyBtn, pasteBtn, editBtn, zipBtn, extractBtn, infoBtn, propertiesBtn, backupBtn);

        VBox toolbarsContainer = new VBox();
        ToolBar toolbar1 = new ToolBar(createFolderBtn, createFileBtn, removeBtn, renameBtn, undoBtn, redoBtn, searchBar, toggleToolbar2Btn);

        toggleToolbar2Btn.setOnAction(e -> toggleToolbar(toolbar2, toggleToolbar2Btn,toolbarsContainer));

        toolbarsContainer.getChildren().addAll(toolbar1);
        toolbarsContainer.getChildren().add(new Separator());

        HBox.setHgrow(toolbar1, Priority.ALWAYS);
        HBox.setHgrow(searchBar, Priority.ALWAYS);

        setTop(toolbarsContainer);
        setRight(null);
    }

    private void toggleToolbar(ToolBar toolbar, ToggleButton toggleButton,VBox toolbarsContainer) {
        if (toggleButton.isSelected()) {
            toolbarsContainer.getChildren().add(toolbar);
        } else {
            toolbarsContainer.getChildren().remove(toolbar);
        }
    }

    private void initializeTreeView() {
        root = new TreeItem<>(model.getRoot().element());
        guiTreeView = new TreeView<>(root);
        setLeft(guiTreeView);
        guiTreeView.getSelectionModel().select(root);
        guiTreeView.getSelectionModel().getSelectedItem().setExpanded(true);
    }

    private void initializeDocumentGrid() {
        documentGrid = new GridPane();
        documentGrid.setHgap(25);
        documentGrid.setVgap(10);
        documentGrid.setPadding(new Insets(10, 0, 0, 20));

        setCenter(documentGrid);
    }

    private void initializeDocumentContent() {
        documentContent = new TextArea();
        documentContent.setEditable(false);
        documentContent.setPrefWidth(300);
        setRight(documentContent);
    }


    public void setTriggers(GUIController controller) {
        createFolderBtn.setOnAction((ActionEvent event) -> {
            controller.doCreateFolder();

        });
        createFileBtn.setOnAction((ActionEvent event) -> {
            controller.doCreateFile();

        });

        renameBtn.setOnAction((ActionEvent event) -> {
            controller.doRename();
        });
        copyBtn.setOnAction((ActionEvent event) -> {
            TreeItem<Document> selectedTreeItem = controller.getSelectedItem();
            if (selectedTreeItem != null) {
                controller.doCopy();
            } else {
                System.err.println("No item selected for copy operation.");
            }
        });


        pasteBtn.setOnAction((ActionEvent event) -> {
            TreeItem<Document> selectedTreeItem = controller.getSelectedItem();
            if (selectedTreeItem != null) {
                controller.doPaste();
            } else {
                System.err.println("Select a directory to paste.");
            }
        });


        removeBtn.setOnAction((ActionEvent event) -> {

            controller.doRemove();
        });

        editBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doEdit();
            } catch (IOException e) {
                System.err.println("Error executing doEdit: " + e.getMessage());
            }
        });
        undoBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doUndo();

            } catch (IOException e) {
                System.err.println("Error executing doUndo: " + e.getMessage());
            }
        });
        redoBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doRedo();

            } catch (IOException e) {
                System.err.println("Error executing doRedo: " + e.getMessage());
            }
        });

        moveBtn.setOnAction((ActionEvent event) -> {
            controller.doMove();
        });

        zipBtn.setOnAction((ActionEvent event) -> {
            controller.doZip();
        });

        extractBtn.setOnAction((ActionEvent event) -> {
            controller.doExtract();
        });

        infoBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doInfo();
            } catch (IOException e) {
                System.err.println("Error executing doInfo: " + e.getMessage());
            }
        });

        propertiesBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doProperties();
            } catch (IOException e) {
                System.err.println("Error executing doProperties: " + e.getMessage());
            }
        });

        backupBtn.setOnAction((ActionEvent event) -> {
            try {
                controller.doBackUp();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        guiTreeView.setOnMouseClicked(event -> {
            TreeItem<Document> selectedItem = guiTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                controller.handleTreeViewItemClick(selectedItem);
                updateDocumentContent(selectedItem.getValue());

            }
        });
    }

    public void updateDocumentContent(Document document) {
        if (document instanceof MyFile myFile) {
            documentContent.setText("File: " + myFile.getName() + "\n\n" + myFile.getContent());
        } else {
            documentContent.clear();
        }
    }

}