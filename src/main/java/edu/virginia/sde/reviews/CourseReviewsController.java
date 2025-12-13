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
    @FXML
    public Button myReviewButton;

    private User loggedUser;
    private ReviewService reviewService;
    private Course currentCourse;

    // The review object belonging to the current user (if one exists)
    private Review myExistingReview;

    private ObservableList<Review> reviewsData = FXCollections.observableArrayList();

    public void initialize() {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // 1. Setup Table Columns
        ratingCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRating()));
        timestampCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getTimestamp()));
        commentCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getComment()));

        reviewTable.setItems(reviewsData);

        reviewTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayReview(newSelection);
            } else {
                resetFormState();
            }
        });
    }

    public void setContext(User user, Course course) {
        this.loggedUser = user;
        this.currentCourse = course;
        this.reviewService = new ReviewService(user);

        courseInfoLabel.setText(course.getSubject() + " " + course.getCourseNumber() + ": " + course.getCourseName());

        refreshReviews();
    }

    private void refreshReviews() {
        reviewsData.clear();
        reviewsData.addAll(reviewService.getReviewsForCourse(currentCourse));

        double avg = reviewService.getCourseAverageRating(currentCourse);
        averageRatingLabel.setText(String.format("Average Rating: %.2f", avg));

        try {
            myExistingReview = reviewService.getUserReview(currentCourse, loggedUser);
        } catch (ReviewDoesNotExistException e) {
            myExistingReview = null;
        }
        resetFormState();
    }

    /**
     * Logic to display a specific review in the bottom panel.
     * If it's NOT mine, lock it (Read Only).
     * If it IS mine, unlock it (Edit Mode).
     */
    private void displayReview(Review review) {
        ratingChoiceBox.setValue(review.getRating());
        commentArea.setText(review.getComment());

        boolean isMyReview = review.getUser().getUsername().equals(loggedUser.getUsername());

        if (isMyReview) {
            saveButton.setVisible(true);
            saveButton.setText("Update Review");
            deleteButton.setVisible(true);
            myReviewButton.setVisible(false);
            if (myReviewButton != null) {
                myReviewButton.setVisible(false);
            }
            commentArea.setEditable(true);
            ratingChoiceBox.setDisable(false);
        } else {
            saveButton.setVisible(false);
            deleteButton.setVisible(false);
            myReviewButton.setVisible(true);

            if (myExistingReview != null) {
                myReviewButton.setText("Edit My Review");
            } else {
                myReviewButton.setText("Write a Review");
            }

            commentArea.setEditable(false);
            ratingChoiceBox.setDisable(true);
        }
    }

    /**
     * Resets the form to its default state:
     * - If I have a review -> Edit Mode
     * - If I don't -> Add Mode
     */
    private void resetFormState() {
        reviewTable.getSelectionModel().clearSelection(); // Clear table highlight

        if (myReviewButton != null) {
            myReviewButton.setVisible(false);
        }

        commentArea.setEditable(true);
        ratingChoiceBox.setDisable(false);
        saveButton.setVisible(true);

        if (myExistingReview != null) {
            // --- EDIT MODE (Default) ---
            ratingChoiceBox.setValue(myExistingReview.getRating());
            commentArea.setText(myExistingReview.getComment());
            saveButton.setText("Update Review");
            deleteButton.setVisible(true);
        } else {
            // --- ADD MODE (Default) ---
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
    }

    @FXML
    public void handleDelete() {
        if (myExistingReview != null) {
            reviewService.deleteReview(myExistingReview);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Deleted", "Your review has been deleted.");
            myExistingReview = null; // Clear local reference
            refreshReviews();
        }
    }

    @FXML
    public void handleMyReview() {
        resetFormState();
    }

    public void onBack() {
        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(loggedUser);
    }
}