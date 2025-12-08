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
        assertTrue(Profile.usernameExists(this.profile.getUsername()));
    }

    @Test
    public void profileExists_false() {
        Profile.insertProfile(this.profile);
        assertFalse(Profile.usernameExists("not_username"));
    }

    @Test
    public void profileExists_false_emptyDB() {
        assertFalse(Profile.usernameExists(this.profile.getUsername()));
    }

    @Test
    public void profileExists_false_null() {
        assertFalse(Profile.usernameExists(null));
    }

    @Test
    public void getProfile_true() {
        Profile.insertProfile(this.profile);
        assertTrue(Profile.getProfile(this.profile).equals(this.profile));
    }

    @Test
    public void getProfile_null() {
        Profile.insertProfile(this.profile);
        assertFalse(this.profile.equals(Profile.getProfile(null)));
    }
}
