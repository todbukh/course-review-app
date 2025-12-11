package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

public class CourseReviewsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewsApplication.class.getResource("course-reviews.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        if (!User.usernameExists("testUser")) {
            User.insertUser(new User("testUser", "password123"));
        }
        User currentUser = new UserService().login("testUser", "password123");

        Course testCourse = new Course("CS", 3140, "Software Development");
        if (!Course.courseExists(testCourse)) {
            Course.insertCourse(testCourse);
        }
        List<Course> courses = new CourseService().searchCourses("CS", "3140", null);
        Course currentCourse = courses.get(0);

        CourseReviewsController controller = fxmlLoader.getController();
        controller.setContext(currentUser, currentCourse);

        stage.setTitle("Course Reviews");
        stage.setScene(scene);
        stage.show();
    }

}