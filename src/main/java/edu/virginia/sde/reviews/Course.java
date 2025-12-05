package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.Session;

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
        return this.courseNumber == other.courseNumber && this.subject.equals(other.subject);
    }

    public static void insertCourse(Course course) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.persist(course);
        session.getTransaction().commit();
    }

    public static boolean courseExists(Course course) {
        if(course == null) {
            return false;
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Course e WHERE e.subject=:subject AND e.courseName=:course_name AND e.courseNumber=:course_number";

        TypedQuery<Profile> query = session.createQuery(hql, Profile.class);
        query.setParameter("subject", course.getSubject());
        query.setParameter("course_name", course.getCourseName());
        query.setParameter("course_number", course.getCourseNumber());
        List<Profile> profiles = query.getResultList();

        session.close();

        return !(profiles.isEmpty());
    }

    public static List<Course> getCourseList() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Course e";
        TypedQuery<Course> query = session.createQuery(hql, Course.class);
        return query.getResultList();
    }
}
