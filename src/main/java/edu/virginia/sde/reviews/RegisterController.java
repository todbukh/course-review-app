package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private final UserService userService = new UserService();

    @FXML
    private void onSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passConfirm = confirmPasswordField.getText();

        if (username.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Empty", "Please enter your username.");
            return;
        }
        if (password.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Empty", "Please enter your password.");
            return;
        }
        if (passConfirm.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Confirm Password Empty", "Please confirm your password.");
            return;
        }
        if (!password.equals(passConfirm)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match.");
            return;
        }

        UserService.UserCreateResult result = userService.createUser(username, password);
        if (result == UserService.UserCreateResult.FAILED_PASSWORD_TOO_SHORT) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters!");
        } else if (result == UserService.UserCreateResult.FAILED_USERNAME_TAKEN) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Error", "Username is already taken!" );
        } else if (result == UserService.UserCreateResult.SUCCESS) {
            CourseReviewsApplication.setRoot("LoginView.fxml");
        }
    }

    @FXML
    private void onCancel() {
        CourseReviewsApplication.setRoot("LoginView.fxml");
    }
}
