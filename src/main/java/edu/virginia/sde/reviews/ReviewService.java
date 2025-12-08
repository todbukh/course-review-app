package edu.virginia.sde.reviews;

import java.util.*;

/**
 * Handles business logic for the "My Reviews" and "Course Reviews" Scenes
 */
public class ReviewService {

    public List<Review> getReviewsForCourse(Course course) {
        return Review.getReviewsFromCourse(course);
    }
    public boolean submitReview(Course course, Profile user, int Rating, String comment) {
        //FIXME: implement
        return false;
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
