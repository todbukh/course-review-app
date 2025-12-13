package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CourseReviewsController {

    @FXML
    public Label courseInfoLabel;

    @FXML
    public Label averageRatingLabel;

    @FXML
    public ListView<Review> reviewListView;
    @FXML
    public ChoiceBox<Integer> ratingChoiceBox;
    @FXML
    public TextArea commentArea;

    private User loggedUser;
    private ReviewService reviewService;
    private Course currentCourse;
    private ObservableList<Review> reviewsData = FXCollections.observableArrayList();

    public void initialize() {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        reviewListView.setItems(reviewsData);
    }

    public void onBack() {
        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(loggedUser);
    }

    public void setContext(User user, Course course) {
        this.loggedUser = user;
        this.currentCourse = course;
        this.reviewService = new ReviewService(user);
        courseInfoLabel.setText(course.getSubject() + " " + course.getCourseNumber());
        refreshReviews();
    }

    private void refreshReviews() {
        reviewsData.clear();
        reviewsData.addAll(reviewService.getReviewsForCourse(currentCourse));

        double avg = reviewService.getCourseAverageRating(currentCourse);
        averageRatingLabel.setText(String.format("Average Rating: %.2f", avg));
    }

    @FXML
    public void handleSave() {
        if (ratingChoiceBox.getValue() == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Fail", "Please pick a rating.");
            return;
        }

        var result = reviewService.submitReview(
                currentCourse,
                ratingChoiceBox.getValue(),
                commentArea.getText()
        );

        switch (result) {
            case FAILED_USER_ALREADY_REVIEWED:
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Fail", "You have already reviewed this course!");
                return;
            case SUCCESS:
                AlertUtil.showAlert(Alert.AlertType.CONFIRMATION, "Success", "Course Reviews saved!");
                commentArea.clear();
                refreshReviews();
        }
    }
}