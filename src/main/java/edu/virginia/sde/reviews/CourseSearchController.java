package edu.virginia.sde.reviews;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CourseSearchController {

    private final CourseService courseService = new CourseService();
    private User loggedInUser;
    private ReviewService reviewService;

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
    private TableView<Course> courseTable;
    @FXML
    private ObservableList<Course> courseData;
    @FXML
    private TableColumn<Course, String> subjectCol;
    @FXML
    private TableColumn<Course, Integer> numberCol;
    @FXML
    private TableColumn<Course, String> titleCol;
    @FXML
    private TableColumn<Course, String> ratingCol;

    @FXML
    private Button logOutButton;

    @FXML
    public void initialize() {

        subjectCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getSubject())
        );
        numberCol.setCellValueFactory(
                cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCourseNumber())
        );
        titleCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getCourseName())
        );

    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        this.reviewService = new ReviewService(user);
        initRatingColumn();
        initCourses();
    }

    private void initRatingColumn() {
        ratingCol.setCellValueFactory(cell -> {
            double r = reviewService.getCourseAverageRating(cell.getValue());
            return new ReadOnlyObjectWrapper<>(r < 0 ? null : r).asString();
        });
    }

    private void initCourses() {
        List<Course> courses = courseService.getAllCourses();

        courseData = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseData);
    }

    @FXML
    public void onSearch(){
        String subject = subjectField.getText();
        String number = numberField.getText();
        String title = titleField.getText();

        List<Course> courses = courseService.searchCourses(subject, number, title);
        ObservableList<Course> data =
                    FXCollections.observableArrayList(courses);
            courseTable.setItems(data);
    }

    @FXML
    public void onAdd(){
        String subject = subjectField.getText();
        String number = numberField.getText();
        String title = titleField.getText();

        CourseService.CourseAddResult result = courseService.addCourse(subject, number, title);
    }

    @FXML
    public void onLogout() {
        CourseReviewsApplication.switchScene("login.fxml");
    }
}