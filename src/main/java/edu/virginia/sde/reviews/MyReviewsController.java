package edu.virginia.sde.reviews;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.List;

public class MyReviewsController {
    private User loggedUser;
    private ReviewService reviewService;

    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private ObservableList<Review> reviewData;
    @FXML
    private TableColumn<Review, String> subjectCol;
    @FXML
    private TableColumn<Review, Integer> numberCol;
    @FXML
    private TableColumn<Review, String> titleCol;
    @FXML
    private TableColumn<Review, Integer> ratingCol;
    @FXML
    private TableColumn<Review, String> commentCol;

    @FXML
    private void onBack() {
        CourseSearchController controller = CourseReviewsApplication.switchScene("course-search.fxml");
        controller.setLoggedInUser(loggedUser);
    }

    public void setLoggedInUser(User user) {
        this.loggedUser = user;
        this.reviewService = new ReviewService(user);
        initReviews();
        refreshReviews();
    }

    private void initReviews() {

        subjectCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getCourse().getSubject())
        );
        numberCol.setCellValueFactory(
                cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCourse().getCourseNumber())
        );
        titleCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getCourse().getCourseName())
        );
        ratingCol.setCellValueFactory(
                cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRating())
        );
        commentCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getComment())
        );

        reviewTable.setRowFactory(tv -> {
            TableRow<Review> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Review clickedReview = row.getItem();
                    onReviewClick(clickedReview);
                }
            });

            return row;
        });
    }

    private void onReviewClick(Review review) {
        // TODO switch to course review scene
        return;
    }

    private void refreshReviews() {
        List<Review> reviews = reviewService.getLoggedProfileReviews();

        reviewData = FXCollections.observableArrayList(reviews);
        reviewTable.setItems(reviewData);
    }
}
