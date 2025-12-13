package edu.virginia.sde.reviews;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    private UserService userService;

    @FXML
    private Button closeButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.disableProperty().bind(
                usernameField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
        );
    }

    @FXML
    public void onClose() {
        Platform.exit();
    }

    @FXML
    public void onLogin() {
        if (userService == null) {
            userService = new UserService();
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.login(username, password);
        if (user == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter correct username and password!");
            return;
        }

        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(user);
        }

    @FXML
    public void onRegister(){
        CourseReviewsApplication.switchScene("register.fxml");
    }
}