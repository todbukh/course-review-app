package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;

public class CourseSearchController {

    private final CourseService courseService = new CourseService();

    @FXML
    private TextField subjectField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField titleField;

    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;

    @FXML
    public void onSearch(){
        String subject = subjectField.getText();
        String number = numberField.getText();
        String title = titleField.getText();

        List<Course> courses = courseService.searchCourses(subject, number, title);
    }

    @FXML
    public void onAdd(){
        String subject = subjectField.getText();
        String number = numberField.getText();
        String title = titleField.getText();

        CourseService.CourseAddResult result = courseService.addCourse(subject, number, title);
    }
}
