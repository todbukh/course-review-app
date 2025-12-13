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
    private User loggedUser;
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
    private Button myReviewsButton;
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

        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Course clickedCourse = row.getItem();
                    onCourseClick(clickedCourse);
                }
            });

            return row;
        });
    }

    public void setLoggedInUser(User user) {
        this.loggedUser = user;
        this.reviewService = new ReviewService(user);
        initRatingColumn();
        refreshCourses();
    }

    private void initRatingColumn() {
        ratingCol.setCellValueFactory(cell -> {
            double r = reviewService.getCourseAverageRating(cell.getValue());
            return new ReadOnlyObjectWrapper<>(r < 0 ? null : r).asString();
        });
    }

    private void refreshCourses() {
        List<Course> courses = courseService.getAllCourses();

        courseData = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseData);
    }

    private void onCourseClick(Course course) {
        CourseReviewsController controller = CourseReviewsApplication.switchScene("course-reviews.fxml");
        controller.setContext(loggedUser, course);
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

        switch (result) {
            case FAILED_EMPTY_TITLE:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Empty Title", "Title cannot be empty!");
                return;
            case FAILED_TITLE_TOO_LONG:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Title Too Long", "Title cannot be longer than 50 characters!");
                return;
            case FAILED_EMPTY_SUBJECT:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Subject cannot be empty!");
                return;
            case FAILED_INVALID_SUBJECT_CHARS:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Subject cannot contain non-letter characters!");
                return;
            case FAILED_INVALID_SUBJECT_LENGTH:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Subject must be between 2 and 4 characters!");
                return;
            case FAILED_EMPTY_COURSE_NUMBER:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Course number cannot be empty!");
                return;
            case FAILED_INVALID_COURSE_NUMBER_CHARS:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Course number cannot be empty!");
                return;
            case FAILED_INVALID_COURSE_NUMBER_LENGTH:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Subject", "Course number cannot be empty!");
                return;
            case FAILED_COURSE_ALREADY_EXISTS:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Fail", "Course already exists!");
                return;
            case SUCCESS:
                AlertUtil.showAlert(Alert.AlertType.CONFIRMATION, "Success", "Course added successfully!");
                refreshCourses();
                break;
        }
    }

    @FXML
    public void onMyReviews(){
        MyReviewsController controller = CourseReviewsApplication.switchScene("my-reviews.fxml");
        controller.setLoggedInUser(loggedUser);
    }

    @FXML
    public void onLogout() {
        CourseReviewsApplication.switchScene("login.fxml");
    }
}