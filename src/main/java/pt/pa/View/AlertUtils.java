package pt.pa.View;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying various types of alerts.
 */
public class AlertUtils {

    /**
     * Displays an alert with the specified type, title, header, and content.
     *
     * @param alertType The type of the alert (e.g., ERROR, WARNING, INFORMATION).
     * @param title     The title of the alert.
     * @param header    The header text of the alert.
     * @param content   The content text of the alert.
     */
    void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays an ERROR alert with the specified title, header, and content.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     */
    public void showErrorAlert(String title, String header, String content) {
        showAlert(Alert.AlertType.ERROR, title, header, content);
    }

    /**
     * Displays a WARNING alert with the specified title, header, and content.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     */
    public void showWarningAlert(String title, String header, String content) {
        showAlert(Alert.AlertType.WARNING, title, header, content);
    }

    /**
     * Displays an INFORMATION alert with the specified title, header, and content.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     */
    public void showInformationAlert(String title, String header, String content) {
        showAlert(Alert.AlertType.INFORMATION, title, header, content);
    }
}