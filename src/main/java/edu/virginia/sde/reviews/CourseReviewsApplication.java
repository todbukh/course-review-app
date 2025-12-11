package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseReviewsApplication extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        setRoot("LoginView.fxml");
        stage.setTitle("Course Reviews - Login");
        stage.show();
    }

    public static void setRoot(String fxml){
        try {
            FXMLLoader loader = new FXMLLoader(CourseReviewsApplication.class.getResource(fxml));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}