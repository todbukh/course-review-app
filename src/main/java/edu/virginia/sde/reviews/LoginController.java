package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private final UserService userService = new UserService();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

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
            return;
        }

        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(user);

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
        switch(result){
            case FAILED_PASSWORD_TOO_SHORT:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters!");
                return;
            case FAILED_USERNAME_TAKEN:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Username Error", "Username is already taken!" );
                return;
            case SUCCESS:
                AlertUtil.showAlert(Alert.AlertType.CONFIRMATION, "Success", "User Created Successfully!");
                break;
        }
    }
}