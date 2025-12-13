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
        switchScene("login.fxml");
        stage.setTitle("Course Reviews - Login");
        stage.show();
    }

    public static <T> T switchScene(String fxml){
        try {
            FXMLLoader loader = new FXMLLoader(CourseReviewsApplication.class.getResource(fxml));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}