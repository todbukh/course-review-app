package edu.virginia.sde.reviews;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ReviewTest {

    @BeforeEach
    public void clearDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from Review").executeUpdate();
        transaction.commit();
        session.close();
    }

    @Test
    public void updateReview_true() {
        Course course = new Course("CS", 1550, "Intro");
        User user1 = new User("user", "pass");
        Course.insertCourse(course);
        User.insertProfile(user1);
        Review review1 = new Review(
                user1,
                course,
                5,
                "great",
                new Timestamp(System.currentTimeMillis()).toString());
        Review.insertReview(review1);

        Review updatedReview = new Review(
                user1,
                course,
                3,
                "okay",
                new Timestamp(System.currentTimeMillis()).toString());

        Review.updateReview(updatedReview);
        assertTrue(Review.getReviewsFromProfile(user1).contains(updatedReview));
        assertFalse(Review.getReviewsFromProfile(user1).contains(review1));
    }

    @Test
    public void deleteReview_true() {
        Course course = new Course("CS", 1550, "Intro");
        User user1 = new User("user", "pass");
        Course.insertCourse(course);
        User.insertProfile(user1);
        Review review1 = new Review(
                user1,
                course,
                5,
                "great",
                new Timestamp(System.currentTimeMillis()).toString());
        Review.insertReview(review1);

        Review.deleteReview(review1.getCourse(), review1.getUser());

        assertFalse(Review.getReviewsFromProfile(user1).contains(review1));
    }

    @Test
    public void getReviewsFromCourse_true() {
        Course course = new Course("CS", 1550, "Intro");
        Course otherCourse = new Course("CS", 1650, "Not Intro");
        User user1 = new User("user", "pass");
        User user2 = new User("user1", "pass");
        User user3 = new User("user2", "pass");
        Course.insertCourse(course);
        Course.insertCourse(otherCourse);
        User.insertProfile(user1);
        User.insertProfile(user2);
        User.insertProfile(user3);

        Review review1 = new Review(
                user1,
                course,
                5,
                "great",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review2 = new Review(
                user2,
                course,
                1,
                "horrible",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review3 = new Review(
                user3,
                otherCourse,
                3,
                "okay",
                new Timestamp(System.currentTimeMillis()).toString());

        Review.insertReview(review1);
        Review.insertReview(review2);
        Review.insertReview(review3);

        assertTrue(Review.getReviewsFromCourse(course).equals(List.of(review1, review2)));
    }

    @Test
    public void getReviewsFromCourse_false_empty() {
        Course course = new Course("CS", 1550, "Intro");
        Course.insertCourse(course);

        assertTrue(Review.getReviewsFromCourse(course).isEmpty());
    }

    @Test
    public void getReviewFromCourse_isEmpty() {

        Course nonexistentCourse = new Course("MATH", 2550, "Intro");
        assertTrue(Review.getReviewsFromCourse(nonexistentCourse).isEmpty());
    }

    @Test
    public void getAverageRating() {
        Course course = new Course("CS", 1550, "Intro");
        User user1 = new User("user", "pass");
        User user2 = new User("user1", "pass");
        User user3 = new User("user2", "pass");
        Course.insertCourse(course);
        User.insertProfile(user1);
        User.insertProfile(user2);
        User.insertProfile(user3);

        Review review1 = new Review(
                user1,
                course,
                5,
                "great",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review2 = new Review(
                user2,
                course,
                1,
                "horrible",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review3 = new Review(
                user3,
                course,
                3,
                "okay",
                new Timestamp(System.currentTimeMillis()).toString());

        Review.insertReview(review1);
        Review.insertReview(review2);
        Review.insertReview(review3);
        assertEquals(3.0, Review.getAverageRating(course));
    }

    @Test
    public void getReviewsFromUser_true() {
        Course course = new Course("CS", 1550, "Intro");
        Course otherCourse = new Course("CS", 1650, "Not Intro");
        User user1 = new User("user", "pass");
        User user2 = new User("user1", "pass");
        Course.insertCourse(course);
        Course.insertCourse(otherCourse);
        User.insertProfile(user1);
        User.insertProfile(user2);

        Review review1 = new Review(
                user1,
                course,
                5,
                "great",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review2 = new Review(
                user1,
                course,
                1,
                "horrible",
                new Timestamp(System.currentTimeMillis()).toString());
        Review review3 = new Review(
                user2,
                otherCourse,
                3,
                "okay",
                new Timestamp(System.currentTimeMillis()).toString());

        Review.insertReview(review1);
        Review.insertReview(review2);
        Review.insertReview(review3);

        assertTrue(Review.getReviewsFromProfile(user1).equals(List.of(review1, review2)));
    }

}
