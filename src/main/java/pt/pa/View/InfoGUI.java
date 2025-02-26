package pt.pa.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.Document;
import pt.pa.MyFile;
import pt.pa.PFS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * The InfoGUI class represents the graphical user interface for displaying information related to the PFS (Personal File System) model.
 * It includes tabs for changed files, created documents, number of changes, and graphical representations of data using BarCharts.
 * The class also provides tables and charts to present data and allows interaction through buttons and combo boxes.
 *
 */
public class InfoGUI extends VBox {
    private PFS model;
    private InfoController infoController;
    private Stage primaryStage;
    private TableView<MyFile> changedTableView, numChangesTableView;
    private TableView<Document> createdTableView;
    private TabPane tabPane;
    private Tab changedTab, createdTab, numChangesTab, graphTab;
    private VBox changedTabContent, createdTabContent, numChangesTabContent, graphTabContent;
    private BarChart<String, Number> createdFilesChart, changedFilesChart;
    private Button closeButton, createdFilesChartButton, changedFilesChartButton;
    private ComboBox<Integer> yearComboBox;

    /**
     * Constructs an EditGUI with the specified model.
     *
     * @param model The PFS model.
     */
    public InfoGUI(PFS model) {
        this.model = model;
        createPanel();
    }

    /**
     * Initializes and configures the graphical elements of the InfoGUI.
     */
    private void createPanel() {
        primaryStage = new Stage();

        initTabsAndTables();
        configureGraphsTab();
        configureStageAndShow();
    }

    /**
     * Initializes the tabs and tables for displaying changed files, created documents, and the number of changes.
     * Configures the necessary TableView instances and adds them to their respective tabs.
     */
    private void initTabsAndTables() {
        changedTab = new Tab("Changed Files");
        createdTab = new Tab("Created Documents");
        numChangesTab = new Tab("# Changes");
        graphTab = new Tab("Graphs");

        changedTabContent = new VBox();
        createdTabContent = new VBox();
        numChangesTabContent = new VBox();
        graphTabContent = new VBox();

        changedTableView = new TableView<>();
        createdTableView = new TableView<>();
        numChangesTableView = new TableView<>();

        closeButton = new Button("Close");

        configureChangedFilesTable();
        configureCreatedDocumentsTable();
        configureNumChangesTable();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(changedTab, createdTab, numChangesTab, graphTab);

        changedTabContent.getChildren().addAll(changedTableView);
        createdTabContent.getChildren().addAll(createdTableView);
        numChangesTabContent.getChildren().addAll(numChangesTableView);

        changedTab.setContent(changedTabContent);
        createdTab.setContent(createdTabContent);
        numChangesTab.setContent(numChangesTabContent);
        graphTab.setContent(graphTabContent);

        getChildren().addAll(tabPane, closeButton);
    }

    /**
     * Configures the primary stage, sets its properties, and displays the InfoGUI.
     * The stage is set to be non-resizable with the title "Info".
     */
    private void configureStageAndShow() {
        Scene scene = new Scene(this, 500, 600);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Info");

        primaryStage.show();
    }

    /**
     * Define os gatilhos de evento para os botões no painel.
     *
     * @param controller O LastAlteredFilesController para lidar com as ações dos botões.
     */
    public void setTriggers(InfoController controller) {
        changedTab.setOnSelectionChanged(event -> {
            if (changedTab.isSelected()) {
                controller.showChangedFiles();
            }
        });

        createdTab.setOnSelectionChanged(event -> {
            if (createdTab.isSelected()) {
                controller.showCreatedFiles();
            }
        });

        numChangesTab.setOnSelectionChanged(event -> {
            if (numChangesTab.isSelected()) {
                controller.showNumChangedFiles();
            }
        });

        graphTab.setOnSelectionChanged(event -> {
            if (numChangesTab.isSelected()) {
                controller.showCreatedFilesBarChart(2024);
            }
        });

        closeButton.setOnAction(event -> primaryStage.close());
    }

    /**
     * Configures the table for displaying changed files.
     */
    private void configureChangedFilesTable() {
        TableColumn<MyFile, String> fileNameColumn = new TableColumn<>("File Name");
        TableColumn<MyFile, String> lastModifiedDateColumn = new TableColumn<>("Last Modified Date");

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastModifiedDateColumn.setCellValueFactory(cellData -> {
            long creationDate = ((Document) cellData.getValue()).getCreationDate();
            return new SimpleStringProperty(formatLocalDateTime(creationDate).toString());
        });

        fileNameColumn.setPrefWidth(248);
        lastModifiedDateColumn.setPrefWidth(248);
        changedTableView.setPrefHeight(525);

        changedTableView.getColumns().addAll(fileNameColumn, lastModifiedDateColumn);
    }


    /**
     * Configures the table for displaying created documents.
     */
    private void configureCreatedDocumentsTable() {
        TableColumn<Document, String> fileNameColumn = new TableColumn<>("File Name");
        TableColumn<Document, String> lastCreatedDateColumn = new TableColumn<>("Creation Date");

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastCreatedDateColumn.setCellValueFactory(cellData -> {
            long creationDate = ((Document) cellData.getValue()).getCreationDate();
            return new SimpleStringProperty(formatLocalDateTime(creationDate).toString());
        });

        fileNameColumn.setPrefWidth(248);
        lastCreatedDateColumn.setPrefWidth(248);
        createdTableView.setPrefHeight(525);

        createdTableView.getColumns().addAll(fileNameColumn, lastCreatedDateColumn);
    }

    /**
     * Configures the table for displaying the number of changes.
     */
    private void configureNumChangesTable() {
        TableColumn<MyFile, Integer> changeCountColumn = new TableColumn<>("# Changes");
        changeCountColumn.setCellValueFactory(new PropertyValueFactory<>("changes"));

        TableColumn<MyFile, Integer> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        changeCountColumn.setPrefWidth(248);
        totalColumn.setPrefWidth(248);
        numChangesTableView.setPrefHeight(525);

        numChangesTableView.getColumns().addAll(changeCountColumn, totalColumn);
        getNumChangesTableView().refresh();
    }

    /**
     * Configures the Graphs tab with BarCharts and buttons.
     */
    private void configureGraphsTab() {
        createdFilesChart = createBarChart("Created Files per Month", "Month", "Number of Files");
        changedFilesChart = createBarChart("Changed Files per Month", "Month", "Number of Changes");

        Button createdFilesChartButton = new Button("Files");
        Button changedFilesChartButton = new Button("Folder");

        ComboBox<Integer> yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(2019, 2020, 2021, 2022, 2023, 2024);
        yearComboBox.setValue(2024);
        yearComboBox.setOnAction(event -> {
            infoController.showCreatedFilesBarChart(getSelectedYear());
            infoController.showChangedFilesBarChart(getSelectedYear());
        });

        createdFilesChartButton.setOnAction(event -> infoController.showCreatedFilesBarChart(2024));
        changedFilesChartButton.setOnAction(event -> infoController.showChangedFilesBarChart(2024));
        yearComboBox.setOnAction(event -> infoController.showDocumentsBySelectedYear());

        HBox buttonsBox = new HBox(5);
        buttonsBox.getChildren().addAll(createdFilesChartButton, changedFilesChartButton, yearComboBox);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        VBox.setVgrow(buttonsBox, Priority.NEVER);
        VBox.setVgrow(createdFilesChart, Priority.ALWAYS);

        graphTabContent.getChildren().addAll(buttonsBox, createdFilesChart, changedFilesChart);

        graphTab.setContent(graphTabContent);
    }

    /**
     * Creates a BarChart with the specified title, x-axis label, and y-axis label.
     *
     * @param title      The title of the BarChart.
     * @param xAxisLabel The label for the x-axis.
     * @param yAxisLabel The label for the y-axis.
     * @return The created BarChart.
     */
    private BarChart<String, Number> createBarChart(String title, String xAxisLabel, String yAxisLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle(title);
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);

        return barChart;
    }

    /**
     * Obtém a TableView da interface gráfica.
     *
     * @return A TableView da interface gráfica.
     */
    public TableView<MyFile> getChangedTableView() {
        return changedTableView;
    }

    public TableView<Document> getCreatedTableView() {
        return createdTableView;
    }

    public TableView<MyFile> getNumChangesTableView() {
        return numChangesTableView;
    }

    public BarChart<String, Number> getCreatedFilesChart() {
        return createdFilesChart;
    }

    public BarChart<String, Number> getChangedFilesChart() {
        return changedFilesChart;
    }

    /**
     * Converts a Unix timestamp to LocalDateTime.
     *
     * @param timestamp The Unix timestamp to convert.
     * @return The corresponding LocalDateTime.
     */
    private LocalDateTime unixToLocalDateTime(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp * 1000);
        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Formats a LocalDateTime to a string using a specified date-time formatter.
     *
     * @param timestamp The Unix timestamp to format.
     * @return The formatted date-time string.
     */
    private String formatLocalDateTime(long timestamp) {
        LocalDateTime dateTime = unixToLocalDateTime(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Gets the selected year from the yearComboBox.
     *
     * @return The selected year.
     */
    public int getSelectedYear() {
        return yearComboBox.getValue();
    }

    /**
     * Gets the yearComboBox used in the GUI.
     *
     * @return The ComboBox<Integer> representing the year selection.
     */
    public ComboBox<Integer> getYearComboBox() {
        return yearComboBox;
    }
}
