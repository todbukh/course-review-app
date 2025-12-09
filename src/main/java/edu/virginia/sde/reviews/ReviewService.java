package edu.virginia.sde.reviews;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles business logic for the "My Reviews" and "Course Reviews" Scenes
 * @author Todd Burged
 */
public class ReviewService {
    /** Possible outcomes when attempting to submit or edit a review. */
    public enum ReviewResult {
        /** Rating was not between 1 and 5 */
        FAILED_INVALID_RATING,
        /** The currently logged-in user has already submitted a review for this course */
        FAILED_USER_ALREADY_REVIEWED,
        /** The currently logged-in user attempted to edit or delete a review that is not his own. */ // This should not happen, but it's here just in case
        FAILED_UNAUTHORIZED_USER,
        SUCCESS
    }
    private final User loggedUser;

    /**
     * Constructs a new ReviewService instance.
     *
     * @param loggedUser The {@link User} of the user currently logged into the application.
     */
    public ReviewService(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Retrieves all reviews written by the currently logged-in user.
     *
     * @return A {@code List} of {@link Review} objects associated with the logged-in user.
     * @see Review#getReviewsFromProfile(User)  
     */
    public List<Review> getLoggedProfileReviews() {
        return Review.getReviewsFromProfile(loggedUser);
    }
    /**
     * Retrieves all reviews associated with a specific course.
     *
     * @param course The {@link Course} for which the reviews are being requested
     * @return A {@code List} of {@link Review} objects for the specified course.
     * @see Review#getReviewsFromCourse(Course)
     */
    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }
    /**
     * Attempts to submit a new review for a specified course.
     * The submission is only successful if the rating is valid (1-5) and the user
     * has not already submitted a review for that course. The current timestamp
     * is generated on submission.
     *
     * @param course The {@link Course} being reviewed.
     * @param rating The integer rating (1-5).
     * @param comment The comment for the review.
     * @return A {@link ReviewResult} indicating the outcome of the submission.
     */
    public ReviewResult submitReview(Course course, int rating, String comment) {
        if (!isRatingValid(rating)) return ReviewResult.FAILED_INVALID_RATING;
        if (getLoggedProfileReviews().stream().anyMatch(r -> r.getCourse().equals(course)))
            return ReviewResult.FAILED_USER_ALREADY_REVIEWED;
        Review review = new Review(loggedUser, course, rating, comment, getCurrentTimestampString());
        Review.insertReview(review);
        return ReviewResult.SUCCESS;
    }

    /**
     * Attempts to update both the rating and comment of an existing review.
     * The rating must be valid (1-5), and the review's timestamp is updated to the current time upon successful edit.
     * <i>NOTE:</i> Updating a review creates a new Review Object with a new ID and deletes the old one.
     * @param oldReview The {@link Review} object that the user selected for editing.
     * @param newRating The new integer rating (1-5).
     * @param newComment The new comment.
     * @return A {@link ReviewResult} indicating the outcome of the edit.
     */
    public ReviewResult editReview(Review oldReview, int newRating, String newComment) {
        if (!isRatingValid(newRating)) return ReviewResult.FAILED_INVALID_RATING;
        if (!loggedUser.equals(oldReview.getUser())) return ReviewResult.FAILED_UNAUTHORIZED_USER;
        String timestampString = getCurrentTimestampString();
        Review newReview = new Review(loggedUser, oldReview.getCourse(), newRating, newComment, timestampString);
        Review.updateReview(newReview);
        return ReviewResult.SUCCESS;
    }
    /**
     * Attempts to update the rating of an existing review.
     * The rating must be valid (1-5), and the review's timestamp is updated to the current time upon successful edit.
     * <i>NOTE:</i> Updating a review creates a new Review Object with a new ID and deletes the old one.
     * @param oldReview The {@link Review} object that the user selected for editing.
     * @param newRating The new integer rating (1-5).
     * @return A {@link ReviewResult} indicating the outcome of the edit.
     */
    public ReviewResult editReview(Review oldReview, int newRating) {
        if (!isRatingValid(newRating)) return ReviewResult.FAILED_INVALID_RATING;
        if (!loggedUser.equals(oldReview.getUser())) return ReviewResult.FAILED_UNAUTHORIZED_USER;
        String timestampString = getCurrentTimestampString();
        Review newReview = new Review(loggedUser, oldReview.getCourse(), newRating, oldReview.getComment(), timestampString);
        Review.updateReview(newReview);
        return ReviewResult.SUCCESS;
    }
    /**
     * Attempts to update the comment of an existing review.
     * The review's timestamp is updated to the current time upon successful edit.
     * <i>NOTE:</i> Updating a review creates a new Review Object with a new ID and deletes the old one.
     * @param oldReview The {@link Review} object that the user selected for editing.
     * @param newComment The new comment.
     * @return A {@link ReviewResult} indicating the outcome of the edit.
     */
    public ReviewResult editReview(Review oldReview, String newComment) {
        if (!loggedUser.equals(oldReview.getUser())) return ReviewResult.FAILED_UNAUTHORIZED_USER;
        String timestampString = getCurrentTimestampString();
        Review newReview = new Review(loggedUser, oldReview.getCourse(), oldReview.getRating(), newComment, timestampString);
        Review.updateReview(newReview);
        return ReviewResult.SUCCESS;
    }

    /**
     * Deletes the review from the database that the logged-in user had made.
     *
     * @param review the {@link Review} to delete selected by the user
     * @return A {@link ReviewResult} indicating the outcome of the deletion.
     */
    public ReviewResult deleteReview(Review review) {
        if (!loggedUser.equals(review.getUser())) return ReviewResult.FAILED_UNAUTHORIZED_USER;
        Review.deleteReview(review.getCourse(), loggedUser);
        return ReviewResult.SUCCESS;
    }

    /**
     * Calculates the average rating for a specified course.
     * The result is rounded to exactly two decimal places. Returns 0.0 if no reviews exist for the course.
     *
     * @param course The {@link Course} for which the average rating is calculated.
     * @return The average rating rounded to two decimal places.
     */
    public double getCourseAverageRating(Course course) {
        double avg = Review.getAverageRating(course);
        return Math.round(avg * 100.0) / 100.0;
    }

    private String getCurrentTimestampString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return LocalDateTime.now().format(dtf);
    }

    private boolean isRatingValid(int Rating) {
        return Rating >= 1 && Rating <= 5;
    }
}
