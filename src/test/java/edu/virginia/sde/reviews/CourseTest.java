package edu.virginia.sde.reviews;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseTest {


    @BeforeEach
    public void clearDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
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

}
