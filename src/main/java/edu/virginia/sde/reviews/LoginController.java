package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> handleLogin());
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Username Empty", "Please enter your username");
            return;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Password Empty", "Please enter your password");
            return;
        }

        User user = userService.login(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.CONFIRMATION, "Login Failed", "Please enter correct username and password");
        }
    }
}