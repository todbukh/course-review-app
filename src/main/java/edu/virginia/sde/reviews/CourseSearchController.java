package edu.virginia.sde.reviews;

import javafx.application.Platform;
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

    public enum CourseSearchMode {
        SEARCH,
        ADD
    }

    private CourseSearchMode mode = CourseSearchMode.SEARCH;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField subjectField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField titleField;

    @FXML
    private Button primaryButton;
    @FXML
    private Button secondaryButton;

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
    private TableColumn<Course, Double> ratingCol;

    @FXML
    private Button myReviewsButton;
    @FXML
    private Button logOutButton;

    @FXML
    public void initialize() {
        setupTable();
        setupActions();
    }

    public void setLoggedInUser(User user) {
        this.loggedUser = user;
        this.reviewService = new ReviewService(user);
        setupColumns();
        refreshCourses();
        Platform.runLater(() -> {
            courseTable.refresh();
        });
    }

    private void setupTable() {
        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        subjectCol.setStyle("-fx-alignment: CENTER;");
        numberCol.setStyle("-fx-alignment: CENTER;");
        titleCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.setStyle("-fx-alignment: CENTER;");
    }

    private void setupColumns() {
        subjectCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getSubject())
        );
        numberCol.setCellValueFactory(
                cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCourseNumber())
        );
        titleCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getCourseName())
        );
        ratingCol.setCellValueFactory(cell -> {
            double r = reviewService.getCourseAverageRating(cell.getValue());
            return new ReadOnlyObjectWrapper<>(r);
        });
        ratingCol.setCellFactory(col -> createStarCell());
    }

    private TableCell<Course, Double> createStarCell() {
        return new TableCell<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty) {
                    setText(null);
                    setTooltip(null);
                } else if (rating == 0.0) {
                    setText("N/A");
                    tooltip.setText("No rating yet");
                    setTooltip(tooltip);
                } else {
                    int fullStars = (int) Math.floor(rating);
                    StringBuilder sb = new StringBuilder();
                    if (fullStars > 0) { sb.append("★".repeat(fullStars)); }
                    if (rating - fullStars >= 0.5) { sb.append("☆"); }
                    setText(sb.toString());
                    tooltip.setText(String.format("%.2f / 5.0", rating));
                    setTooltip(tooltip);
                }
            }
        };
    }

    private void setupActions() {
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
    public void onPrimary(){
        switch(mode) {
            case SEARCH:
                SearchCourse();
                break;
            case ADD:
                AddCourse();
                break;
        }
        clearInputs();
    }

    @FXML
    public void onSecondary(){
        switch(mode) {
            case SEARCH:
                mode = CourseSearchMode.ADD;
                titleLabel.setText("Add Course");
                primaryButton.setText("Add");
                secondaryButton.setText("Cancel");
                break;
            case ADD:
                mode = CourseSearchMode.SEARCH;
                titleLabel.setText("Search Course");
                primaryButton.setText("Search");
                secondaryButton.setText("+ Add Course");
                break;
        }
        clearInputs();
    }

    private void SearchCourse(){
        String subject = subjectField.getText();
        String number = numberField.getText();
        String title = titleField.getText();

        List<Course> courses = courseService.searchCourses(subject, number, title);
        ObservableList<Course> data =
                    FXCollections.observableArrayList(courses);
            courseTable.setItems(data);
    }

    private void AddCourse(){
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
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Course Number", "Course number cannot be empty!");
                return;
            case FAILED_INVALID_COURSE_NUMBER_CHARS:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Course Number", "Course number cannot contain non-numeric characters!");
                return;
            case FAILED_INVALID_COURSE_NUMBER_LENGTH:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Course Number", "Course number must be 4 digits long!");
                return;
            case FAILED_COURSE_ALREADY_EXISTS:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Fail", "Course already exists!");
                return;
            case SUCCESS:
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully!");
                refreshCourses();
                break;
        }
    }

    private void clearInputs() {
        subjectField.clear();
        numberField.clear();
        titleField.clear();
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