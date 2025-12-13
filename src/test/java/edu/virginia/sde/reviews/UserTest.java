package edu.virginia.sde.reviews;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void clearDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        transaction.commit();
        session.close();
    }

    @BeforeEach
    public void createUser() {
        this.user = new User("username", "password");
    }

    @Test
    public void insertData() {
        User.insertUser(this.user);
        assertTrue(User.usernameExists(this.user.getUsername()));
    }

    @Test
    public void UserExists_false() {
        User.insertUser(this.user);
        assertFalse(User.usernameExists("not_username"));
    }

    @Test
    public void UserExists_false_emptyDB() {
        assertFalse(User.usernameExists(this.user.getUsername()));
    }

    @Test
    public void UserExists_false_null() {
        assertFalse(User.usernameExists(null));
    }

    @Test
    public void getUser_true() {
        User.insertUser(this.user);
        assertTrue(User.getUser(this.user).equals(this.user));
    }

    @Test
    public void getUser_null() {
        User.insertUser(this.user);
        assertFalse(this.user.equals(User.getUser(null)));
    }
}
