package edu.virginia.sde.reviews;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void showAlert(Alert.AlertType alertType, String title, String message){
        showAlert(alertType, title, message, false);
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message, boolean wait){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (wait){
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

}
