package edu.virginia.sde.reviews;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseTest {


    @BeforeEach
    public void clearDB() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        if(session.getTransaction().isActive()) {
            transaction = session.getTransaction();
        } else {
            transaction = session.beginTransaction();
        }
        session.createQuery("delete from Course").executeUpdate();
        transaction.commit();
        session.close();
    }

    @Test
    public void courseExists(){
        Course course = new Course("CS", 1000, "Intro to Computer Science");
        Course.insertCourse(course);

        assertTrue(Course.courseExists(course));
    }

    @Test
    public void getCourseList() {
        Course course1 = new Course("CS", 1000, "Intro to Computer Science");
        Course course2 = new Course("CS", 2000, "DSA");
        Course course3 = new Course("HS", 1000, "Intro To History");
        Course course4 = new Course("MATH", 1000, "Intro to Math");
        Course.insertCourse(course1);
        Course.insertCourse(course2);
        Course.insertCourse(course3);
        Course.insertCourse(course4);

        assertTrue(Course.getCourseList().equals(List.of(course1, course2, course3, course4)));
    }

}
