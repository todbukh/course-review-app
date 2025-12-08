package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "timestamp", nullable = false)
    private String timestamp;
    @Column(name = "rating", nullable = false)
    private int rating;
    @Column(name = "comment")
    private String comment;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_name")
    private Course course;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "username")
    private Profile user;

    protected Review() {}

    public Review(Profile user, Course course, int rating, String comment, String timestamp) {
        this.timestamp = timestamp;
        this.rating = rating;
        this.comment = comment;
        this.course = course;
        this.user = user;
    }

    public Profile getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public int getRating() {
        return rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {return id; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Review)) {
            return false;
        }
        Review review = (Review)obj;
        return review.getRating() == this.rating && review.getComment().equals(this.comment) &&  review.getTimestamp().equals(this.timestamp) && review.getUser().getUsername().equals(this.user.getUsername()) && review.getCourse().getCourseName().equals(this.course.getCourseName());
    }

    public static void insertReview(Review review) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.persist(review);
        transaction.commit();
        session.close();
    }

    public static void updateReview(Review initialReview, Review updatedReview) {
        deleteReview(initialReview);
        insertReview(updatedReview);
    }

    public static void deleteReview(Review review) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        String hql = "DELETE FROM Review e WHERE e.course=: course AND e.user=: user";
        MutationQuery query = session.createMutationQuery(hql);
        query.setParameter("course", review.getCourse());
        query.setParameter("user", review.getUser());
        query.executeUpdate();

        transaction.commit();
        session.close();
    }

    public static List<Review> getReviewsFromProfile(Profile profile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Review e WHERE e.user=: user";

        TypedQuery<Review> query = session.createQuery(hql, Review.class);
        query.setParameter("user", profile);

        List<Review> reviews = query.getResultList();
        session.close();
        return reviews;
    }

    public static Review getReviewFromCourseAndProfile(Course course, Profile profile) throws ReviewDoesNotExistException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Review e WHERE e.course=: course AND e.user =: user";

        TypedQuery<Review> query = session.createQuery(hql, Review.class);
        query.setParameter("course", course);
        query.setParameter("user", profile);

        List<Review> reviews = query.getResultList();
        session.close();
        if(reviews.isEmpty()) {
            throw new ReviewDoesNotExistException("Review does not exist");
        }
        return reviews.getFirst();
    }

    protected static List<Review> getReviewsFromCourse(Course course) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Review e WHERE e.course=: course";

        TypedQuery<Review> query = session.createQuery(hql, Review.class);
        query.setParameter("course", course);

        List<Review> reviews = query.getResultList();
        session.close();
        return reviews;
    }
    public static double getAverageRating(Course course) throws IllegalStateException {
        if (Course.courseExists(course)) {
            List<Review> courseReviews = Review.getReviewsFromCourse(course);

            return courseReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        } else {
            throw new IllegalStateException("Course must be in database!");
        }
    }
}
