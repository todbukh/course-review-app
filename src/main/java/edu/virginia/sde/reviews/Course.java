package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name="course_name", nullable = false)
    private String courseName;
    @Column(name = "course_number", nullable = false)
    private int courseNumber;

    protected Course() {}

    public Course(String subject, int courseNumber, String courseName) {
        this.courseNumber = courseNumber;
        this.subject = subject;
        this.courseName = courseName;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Course)) {
            return false;
        }
        Course other = (Course)obj;
        return this.courseNumber == other.courseNumber && this.subject.equals(other.subject) && this.courseName.equals(other.courseName);
    }

    public static void insertCourse(Course course) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            if(session.getTransaction().isActive()) {
                transaction = session.getTransaction();
            } else {
                transaction = session.beginTransaction();
            }
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static boolean courseExists(Course course) {
        if(course == null) {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = new ArrayList<User>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM Course e WHERE e.subject=:subject AND e.courseName=:course_name AND e.courseNumber=:course_number";

            TypedQuery<User> query = session.createQuery(hql, User.class);
            query.setParameter("subject", course.getSubject());
            query.setParameter("course_name", course.getCourseName());
            query.setParameter("course_number", course.getCourseNumber());
            users = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return !(users.isEmpty());
    }

    public static List<Course> getCourseList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Course> courses = new ArrayList<Course>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM Course e";
            TypedQuery<Course> query = session.createQuery(hql, Course.class);
            courses = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return courses;
    }
}
