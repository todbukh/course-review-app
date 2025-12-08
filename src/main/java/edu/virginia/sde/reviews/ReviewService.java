package edu.virginia.sde.reviews;

import java.util.*;
import java.sql.Timestamp;
import java.text.

/**
 * Handles business logic for the "My Reviews" and "Course Reviews" Scenes
 */
public class ReviewService {

    public static enum ReviewResult {
        FAILED_INVALID_RATING,
        FAILED_USER_ALREADY_REVIEWED,
        SUCCESS
    }

    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }

    public ReviewResult submitReview(Course course, Profile user, int rating, String comment) {
        if (!isRatingValid(rating)) return ReviewResult.FAILED_INVALID_RATING;
        // FIXME: need boolean DB method to see if user has reviewed this course
        // if (userHasReviewedCourse) return FAILED_USER_ALREADY_REVIEWED
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        Review review = new Review(user, course, rating, timestamp, comment);
        Review.insertReview(review);
        return ReviewResult.SUCCESS;
    }

    private boolean isRatingValid(int Rating) {
        return Rating >= 1 && Rating <= 5;
    }

    public boolean editReview(int reviewId, int newRating, String newComment) {
        // FIXME: implement
        return false;
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
        //FIXME: implement
        return null;
    }
}
