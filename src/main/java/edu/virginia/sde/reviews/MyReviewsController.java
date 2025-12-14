package edu.virginia.sde.reviews;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    public void onBack() {
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
        setupTable();
        setupColumns();
        setupActions();
    }

    private void setupTable() {
        reviewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        subjectCol.setStyle("-fx-alignment: CENTER;");
        numberCol.setStyle("-fx-alignment: CENTER;");
        titleCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.setStyle("-fx-alignment: CENTER;");
        commentCol.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10");
    }

    private void setupColumns() {
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
        ratingCol.setCellFactory(col -> createStarCell());
        commentCol.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getComment())
        );
    }

    private TableCell<Review, Integer> createStarCell() {
        return new TableCell<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText("★".repeat(rating));
                    tooltip.setText(rating + " / 5");
                    setTooltip(tooltip);
                }
            }
        };
    }

    private void setupActions() {
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
        CourseReviewsController controller = CourseReviewsApplication.switchScene("course-reviews.fxml");
        controller.setContext(loggedUser, review.getCourse());
    }

    private void refreshReviews() {
        List<Review> reviews = reviewService.getLoggedProfileReviews();

        reviewData = FXCollections.observableArrayList(reviews);
        reviewTable.setItems(reviewData);
    }
}
