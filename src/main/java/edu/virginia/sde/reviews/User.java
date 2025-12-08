package edu.virginia.sde.reviews;
import jakarta.persistence.*;
import org.hibernate.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    protected User() {}

    public User(String username, String password) {
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
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User)obj;
        return this.username.equals(user.username) && this.password.equals(user.password);
    }

    public static void insertProfile(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public static User getProfile(User user) {
        if(user == null) {
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = new ArrayList<User>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM User e WHERE e.username=:username AND e.password=:password";

            TypedQuery<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", user.getUsername());
            query.setParameter("password", user.getPassword());
            users = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally{
            session.close();
        }
        return (users.isEmpty()) ?  null : (User) users.getFirst();
    }

    public static boolean usernameExists(String username) {
        if(username == null) {
            return false;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = new ArrayList<User>();
        try {
            session.beginTransaction();

            String hql = "SELECT e FROM User e WHERE e.username=:username";

            TypedQuery<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            users = query.getResultList();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return (!users.isEmpty()) && (users.getFirst().username.equals(username));
    }


}
