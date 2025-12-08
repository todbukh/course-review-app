package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    protected Profile() {}

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }
        Profile profile = (Profile)obj;
        return this.username.equals(profile.username) && this.password.equals(profile.password);
    }

    public static void insertProfile(Profile profile) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(profile);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public static Profile getProfile(Profile profile) {
        if(profile == null) {
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Profile> profiles = new ArrayList<Profile>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM Profile e WHERE e.username=:username AND e.password=:password";

            TypedQuery<Profile> query = session.createQuery(hql, Profile.class);
            query.setParameter("username", profile.getUsername());
            query.setParameter("password", profile.getPassword());
            profiles = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally{
            session.close();
        }
        return (profiles.isEmpty()) ?  null : (Profile) profiles.getFirst();
    }

    public static boolean usernameExists(String username) {
        if(username == null) {
            return false;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Profile> profiles = new ArrayList<Profile>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM Profile e WHERE e.username=:username";

            TypedQuery<Profile> query = session.createQuery(hql, Profile.class);
            query.setParameter("username", username);
            profiles = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return (!profiles.isEmpty()) && (profiles.getFirst().username.equals(username));
    }


}
