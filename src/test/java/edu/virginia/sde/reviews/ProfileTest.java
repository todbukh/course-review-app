package edu.virginia.sde.reviews;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void clearDB() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from Profile").executeUpdate();
        transaction.commit();
        session.close();
    }

    @BeforeEach
    public void createProfile() {
        this.profile = new Profile("username", "password");
    }

    @Test
    public void insertData() {
        Profile.insertProfile(this.profile);
        assertTrue(Profile.profileExists(this.profile));
    }

    @Test
    public void profileExists_false() {
        Profile.insertProfile(this.profile);
        assertFalse(Profile.profileExists(new Profile("not_username", "not_password")));
    }

    @Test
    public void profileExists_false_emptyDB() {
        assertFalse(Profile.profileExists(this.profile));
    }

    @Test
    public void profileExists_false_null() {
        assertFalse(Profile.profileExists(null));
    }
}
