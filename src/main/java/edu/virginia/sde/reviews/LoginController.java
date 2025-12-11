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
    @FXML
    private Button registerButton;

    private final UserService userService = new UserService();

    @FXML
    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Empty", "Please enter your username.");
            return;
        }
        if (password.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Empty", "Please enter your password.");
            return;
        }

        User user = userService.login(username, password);
        if (user == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter correct username and password!");
        }
    }

    @FXML
    public void onRegister(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Empty", "Please enter your username.");
            return;
        }
        if (password.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Empty", "Please enter your password.");
            return;
        }

        UserService.UserCreateResult result = userService.createUser(username, password);
        if (result == UserService.UserCreateResult.FAILED_PASSWORD_TOO_SHORT) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters!");
        } else if (result == UserService.UserCreateResult.FAILED_USERNAME_TAKEN) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Error", "Username is already taken!" );
        } else if (result == UserService.UserCreateResult.SUCCESS) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "User Created Successfully!");
        }
    }
}