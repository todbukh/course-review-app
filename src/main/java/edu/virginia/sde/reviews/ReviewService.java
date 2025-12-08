package edu.virginia.sde.reviews;

import java.util.*;
import java.sql.Timestamp;

/**
 * Handles business logic for the "My Reviews" and "Course Reviews" Scenes
 */
public class ReviewService {
    public static enum ReviewResult {
        FAILED_INVALID_RATING,
        FAILED_USER_ALREADY_REVIEWED,
        SUCCESS
    }
    private final Profile loggedProfile;

    public ReviewService(Profile loggedProfile) {
        this.loggedProfile = loggedProfile;
    }

    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }

    public ReviewResult submitReview(Course course, int rating, String comment) {
        if (!isRatingValid(rating)) return ReviewResult.FAILED_INVALID_RATING;
        if (getLoggedProfileReviews().stream().anyMatch(r -> r.getCourse().equals(course)))
            return ReviewResult.FAILED_USER_ALREADY_REVIEWED;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        Review review = new Review(loggedProfile, course, rating, timestamp, comment);
        Review.insertReview(review);
        return ReviewResult.SUCCESS;
    }

    private boolean isRatingValid(int Rating) {
        return Rating >= 1 && Rating <= 5;
    }

    public ReviewResult editReview(int reviewId, int newRating, String newComment) {
        // FIXME: need DB method to update a review given the reviewId
        if (!isRatingValid(newRating)) return ReviewResult.FAILED_INVALID_RATING;
        // update the review with reviewID with the new rating and comment
        return ReviewResult.SUCCESS;
    }

    public void deleteReview(int reviewId) {
        // FIXME: need DB method to delete a review given the reviewId
        // delete review with reviewId
    }

    public double getCourseAverageRating(Course course) {
        return getReviewsForCourse(course).stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    public List<Review> getLoggedProfileReviews() {
        //FIXME: need DB method to get Reviews by user
        return null;
    }
}
