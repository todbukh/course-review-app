package edu.virginia.sde.reviews;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
    public TableView<Review> reviewTable;
    @FXML
    public TableColumn<Review, Integer> ratingCol;
    @FXML
    public TableColumn<Review, String> timestampCol;
    @FXML
    public TableColumn<Review, String> commentCol;

    @FXML
    public ChoiceBox<Integer> ratingChoiceBox;
    @FXML
    public TextArea commentArea;
    @FXML
    public Button saveButton;
    @FXML
    public Button deleteButton;

    private User loggedUser;
    private ReviewService reviewService;
    private Course currentCourse;

    // If the user has a review, we store it here so we can edit/delete it later
    private Review myExistingReview;

    private ObservableList<Review> reviewsData = FXCollections.observableArrayList();

    public void initialize() {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // --- 1. Setup Table Columns ---
        ratingCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRating()));
        timestampCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getTimestamp()));
        commentCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getComment()));

        reviewTable.setItems(reviewsData);
    }

    public void setContext(User user, Course course) {
        this.loggedUser = user;
        this.currentCourse = course;
        this.reviewService = new ReviewService(user);

        courseInfoLabel.setText(course.getSubject() + " " + course.getCourseNumber() + ": " + course.getCourseName());

        refreshReviews();
        checkIfUserHasReview();
    }

    private void refreshReviews() {
        reviewsData.clear();
        reviewsData.addAll(reviewService.getReviewsForCourse(currentCourse));

        double avg = reviewService.getCourseAverageRating(currentCourse);
        averageRatingLabel.setText(String.format("Average Rating: %.2f", avg));
    }

    /**
     * Checks if the user has already reviewed this course.
     * If yes -> Switch to "Edit Mode" (Populate fields, show Delete button).
     * If no  -> Switch to "Add Mode" (Empty fields, hide Delete button).
     */
    private void checkIfUserHasReview() {
        try {
            myExistingReview = reviewService.getUserReview(currentCourse, loggedUser);

            ratingChoiceBox.setValue(myExistingReview.getRating());
            commentArea.setText(myExistingReview.getComment());
            saveButton.setText("Update Review");
            deleteButton.setVisible(true);

        } catch (ReviewDoesNotExistException e) {
            myExistingReview = null;
            ratingChoiceBox.setValue(null);
            commentArea.clear();
            saveButton.setText("Submit Review");
            deleteButton.setVisible(false);
        }
    }

    @FXML
    public void handleSave() {
        if (ratingChoiceBox.getValue() == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please pick a rating.");
            return;
        }

        String comment = commentArea.getText();
        int rating = ratingChoiceBox.getValue();

        if (myExistingReview != null) {
            // Update existing review
            reviewService.editReview(myExistingReview, rating, comment);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Review updated!");
        } else {
            // Create new review
            reviewService.submitReview(currentCourse, rating, comment);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Review submitted!");
        }

        refreshReviews();
        checkIfUserHasReview(); // Re-check state to update UI
    }

    @FXML
    public void handleDelete() {
        if (myExistingReview != null) {
            reviewService.deleteReview(myExistingReview);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Deleted", "Your review has been deleted.");

            refreshReviews();
            checkIfUserHasReview(); // reset to "Add Mode"
        }
    }

    public void onBack() {
        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(loggedUser);
    }
}