package pt.pa.View;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import pt.pa.Document;
import pt.pa.MyFile;
import pt.pa.PFS;

import java.util.List;
import java.util.Map;

/**
 * The {@code InfoController} class is responsible for handling user interactions and managing
 * the information display in the application. It connects the graphical user
 * interface ({@link InfoGUI}) with the underlying file system model ({@link PFS}) and updates the
 * displayed information based on user input.
 *
 * @author Your Name
 * @version 1.0
 */
public class InfoController {

    private PFS model;
    private InfoGUI infoGUI;

    /**
     * Constructs an {@code InfoController} with the specified model and graphical user interface.
     *
     * @param model   The underlying file system model ({@link PFS}).
     * @param infoGUI The graphical user interface ({@link InfoGUI}).
     */
    public InfoController(PFS model, InfoGUI infoGUI) {
        this.model = model;
        this.infoGUI = infoGUI;
        showChangedFiles();
        infoGUI.setTriggers(this);
    }

    /**
     * Retrieves the last 10 changed files from the model and updates the corresponding table in the GUI.
     */
    private void getLast10ChangedFiles() {
        List<MyFile> lastChangedFiles = model.getLast10Altered();
        getChangedTableView().setItems(FXCollections.observableArrayList(lastChangedFiles));
    }

    /**
     * Retrieves the last 20 created files from the model and updates the corresponding table in the GUI.
     */
    private void getLast20CreatedFiles() {
        List<Document> lastCreatedFiles = model.getLast20CreatedFiles();
        getCreatedTableView().setItems(FXCollections.observableArrayList(lastCreatedFiles));
    }

    /**
     * Display the number of changed files.
     */
    private void getNumChangeFiles() {
        // To be implemented
    }

    /**
     * Displays the last 10 changed files in the GUI.
     */
    public void showChangedFiles() {
        getLast10ChangedFiles();
    }

    /**
     * Displays the last 20 created files in the GUI.
     */
    public void showCreatedFiles() {
        getLast20CreatedFiles();
    }

    /**
     * Placeholder method for future functionality related to the number of changed files.
     */
    public void showNumChangedFiles() {
        getNumChangeFiles();
    }

    /**
     * Displays a bar chart in the GUI representing the created files per month for the specified year.
     *
     * @param year The year for which the bar chart is displayed.
     */
    public void showCreatedFilesBarChart(int year) {
        BarChart<String, Number> chart = infoGUI.getCreatedFilesChart();
        showBarChart(chart, year, model.getCreatedFilesDataByMonth(year));
    }

    /**
     * Displays a bar chart in the GUI representing the changed files per month for the specified year.
     *
     * @param year The year for which the bar chart is displayed.
     */
    public void showChangedFilesBarChart(int year) {
        BarChart<String, Number> chart = infoGUI.getChangedFilesChart();
        showBarChart(chart, year, model.getChangedFilesDataByMonth(year));
    }

    /**
     * Displays documents in the GUI based on the selected year.
     */
    public void showDocumentsBySelectedYear() {
        int selectedYear = infoGUI.getSelectedYear();
        List<Document> documents = model.getDocumentsBySelectedYear(selectedYear);
        getCreatedTableView().setItems(FXCollections.observableArrayList(documents));
    }

    /**
     * Updates the specified bar chart with the provided data.
     *
     * @param chart The bar chart to update.
     * @param year  The year for which the data is displayed.
     * @param data  The data to be displayed in the chart.
     */
    private void showBarChart(BarChart<String, Number> chart, int year, Map<String, Integer> data) {
        chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().add(series);
    }

    /**
     * Retrieves the TableView for changed files from the GUI.
     *
     * @return The TableView for changed files.
     */
    public TableView<MyFile> getChangedTableView() {
        return infoGUI.getChangedTableView();
    }

    /**
     * Retrieves the TableView for created documents from the GUI.
     *
     * @return The TableView for created documents.
     */
    public TableView<Document> getCreatedTableView() {
        return infoGUI.getCreatedTableView();
    }
}
