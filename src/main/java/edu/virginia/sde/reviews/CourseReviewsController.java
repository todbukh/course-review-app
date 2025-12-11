package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CourseReviewsController {

    @FXML
    public Label courseInfoLabel;
    @FXML
    public ListView<Review> reviewListView;
    @FXML
    public ChoiceBox<Integer> ratingChoiceBox;
    @FXML
    public TextArea commentArea;
    @FXML
    public Label errorLabel;

    private ReviewService reviewService;
    private Course currentCourse;
    private ObservableList<Review> reviewsData = FXCollections.observableArrayList();

    public void initialize() {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        reviewListView.setItems(reviewsData);
    }

    public void setContext(User user, Course course) {
        this.currentCourse = course;
        this.reviewService = new ReviewService(user);
        courseInfoLabel.setText(course.getSubject() + " " + course.getCourseNumber());
        refreshReviews();
    }

    private void refreshReviews() {
        reviewsData.clear();
        reviewsData.addAll(reviewService.getReviewsForCourse(currentCourse));
    }

    @FXML
    public void handleSave() {
        if (ratingChoiceBox.getValue() == null) {
            errorLabel.setText("Please pick a rating.");
            return;
        }

        var result = reviewService.submitReview(
                currentCourse,
                ratingChoiceBox.getValue(),
                commentArea.getText()
        );

        if (result == ReviewService.ReviewResult.SUCCESS) {
            errorLabel.setText("");
            commentArea.clear();
            refreshReviews();
        } else {
            errorLabel.setText("Error: " + result.toString());
        }
    }
}