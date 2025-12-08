package edu.virginia.sde.reviews;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public List<Review> getLoggedProfileReviews() {
        //FIXME: need DB method to get Reviews by user
        return null;
    }
    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }

    public ReviewResult submitReview(Course course, int rating, String comment) {
        if (!isRatingValid(rating)) return ReviewResult.FAILED_INVALID_RATING;
        if (getLoggedProfileReviews().stream().anyMatch(r -> r.getCourse().equals(course)))
            return ReviewResult.FAILED_USER_ALREADY_REVIEWED;
        Review review = new Review(loggedProfile, course, rating, comment, getCurrentTimestampString());
        Review.insertReview(review);
        return ReviewResult.SUCCESS;
    }

    public ReviewResult editReview(int reviewId, int newRating, String newComment) {
        // FIXME: need DB method to update a review given the reviewId
        if (!isRatingValid(newRating)) return ReviewResult.FAILED_INVALID_RATING;
        String timestampString = getCurrentTimestampString();
        // update the review with reviewID with the new rating, comment, and timestamp
        return ReviewResult.SUCCESS;
    }

    public void deleteReview(int reviewId) {
        // FIXME: need DB method to delete a review given the reviewId
        // delete review with reviewId
    }

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
