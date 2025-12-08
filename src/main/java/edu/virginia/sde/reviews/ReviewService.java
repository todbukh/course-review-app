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
    private List<Review> loggedProfileReviews;

    public ReviewService(Profile loggedProfile) {
        this.loggedProfile = loggedProfile;
        this.loggedProfileReviews = getReviewsByUser(loggedProfile);
    }

    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }

    public ReviewResult submitReview(Course course, int rating, String comment) {
        if (!isRatingValid(rating)) return ReviewResult.FAILED_INVALID_RATING;
        if (loggedProfileReviews.stream().anyMatch(r -> r.getCourse().equals(course)))
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
        // update review with reviewID with the new rating and comment
        return ReviewResult.SUCCESS;
    }

    public boolean deleteReview(int reviewId) {
        // FIXME: implement
        return false;
    }

    public double getCourseAverageRating(Course course) {
        // FIXME: implement
        return -1.0;
    }

    public List<Review> getReviewsByUser(Profile user) {
        //FIXME: need DB method to get Reviews by user
        return null;
    }
}
