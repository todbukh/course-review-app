package edu.virginia.sde.reviews;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles business logic for the "My Reviews" and "Course Reviews" Scenes
 *
 * @author Todd Burged
 */
public class ReviewService {
    /** Possible outcomes when attempting to submit or edit a review. */
    public enum ReviewResult {
        /** Rating was not between 1 and 5 */
        FAILED_INVALID_RATING,
        /** The currently logged-in user has already submitted a review for this course */
        FAILED_USER_ALREADY_REVIEWED,
        SUCCESS
    }
    private final Profile loggedProfile;

    /**
     * Constructs a new ReviewService instance.
     *
     * @param loggedProfile The {@link Profile} of the user currently logged into the application.
     */
    public ReviewService(Profile loggedProfile) {
        this.loggedProfile = loggedProfile;
    }

    /**
     * Retrieves all reviews written by the currently logged-in user.
     *
     * @return A {@code List} of {@link Review} objects associated with the logged-in user.
     */
    public List<Review> getLoggedProfileReviews() {
        //FIXME: need DB method to get Reviews by user
        return null;
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
        Review review = new Review(loggedProfile, course, rating, comment, getCurrentTimestampString());
        Review.insertReview(review);
        return ReviewResult.SUCCESS;
    }

    /**
     * Attempts to edit an existing review by its ID.
     * The rating must be valid (1-5), and the review's timestamp is updated to the current time upon successful edit.
     *
     * @param reviewId The ID of the review to be updated.
     * @param newRating The new integer rating (1-5).
     * @param newComment The new comment.
     * @return A {@link ReviewResult} indicating the outcome of the edit operation.
     */
    public ReviewResult editReview(int reviewId, int newRating, String newComment) {
        // FIXME: need DB method to update a review given the reviewId
        if (!isRatingValid(newRating)) return ReviewResult.FAILED_INVALID_RATING;
        String timestampString = getCurrentTimestampString();
        // update the review with reviewID with the new rating, comment, and timestamp
        return ReviewResult.SUCCESS;
    }

    /**
     * Deletes a review from the database given its ID.
     *
     * @param reviewId The ID of the review to be deleted.
     */
    public void deleteReview(int reviewId) {
        // FIXME: need DB method to delete a review given the reviewId
        // delete review with reviewId
    }

    /**
     * Calculates the average rating for a specified course.
     * The result is rounded to exactly two decimal places. Returns 0.0 if no reviews exist for the course.
     *
     * @param course The {@link Course} for which the average rating is calculated.
     * @return The average rating rounded to two decimal places.
     */
    public double getCourseAverageRating(Course course) {
        double avg = getReviewsForCourse(course).stream().mapToInt(Review::getRating).average().orElse(0.0);
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
