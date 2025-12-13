package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    private UserService userService;

    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        registerButton.disableProperty().bind(
                usernameField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty()
                                .or(passwordConfirmField.textProperty().isEmpty()))
        );
    }

    public void onBack() {
        CourseReviewsApplication.switchScene("login.fxml");
    }

    public void onRegister() {
        if (userService == null) {
            userService = new UserService();
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!password.equals(passwordConfirmField.getText())) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Passwords Error", "Passwords do not match.");
            return;
        }

        UserService.UserCreateResult result = userService.createUser(username, password);
        switch(result){
            case FAILED_PASSWORD_TOO_SHORT:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters!");
                return;
            case FAILED_USERNAME_TAKEN:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Error", "Username is already taken!" );
                return;
            case SUCCESS:
                // show and wait the alert; user must hit OK to switch to log in scene.
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "User Created Successfully!", true);
                CourseReviewsApplication.switchScene("login.fxml");
                break;
        }
    }
}
