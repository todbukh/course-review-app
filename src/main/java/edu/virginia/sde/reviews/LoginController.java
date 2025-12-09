package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
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

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.login(username, password);
    }
}