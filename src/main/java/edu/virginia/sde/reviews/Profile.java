package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.*;

import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
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

    public static void insertProfile(Profile profile) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.persist(profile);
        session.getTransaction().commit();
        session.close();
    }

    public static boolean profileExists(Profile profile) {
        if(profile == null) {
            return false;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "SELECT e FROM Profile e WHERE e.username=:username AND e.password=:password";

        TypedQuery<Profile> query = session.createQuery(hql, Profile.class);
        query.setParameter("username", profile.getUsername());
        query.setParameter("password", profile.getPassword());
        List<Profile> profiles = query.getResultList();

        session.close();

        return !(profiles.isEmpty());
    }
}
