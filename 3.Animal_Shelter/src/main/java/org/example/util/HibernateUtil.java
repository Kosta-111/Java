package org.example.util;

import lombok.Getter;
import org.example.entities.Animal;
import org.example.entities.Enclosure;
import org.example.entities.Responsible;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    public static void connect() {
        try {
            var config = new Configuration().configure();
            config.addAnnotatedClass(Animal.class)
                .addAnnotatedClass(Enclosure.class)
                .addAnnotatedClass(Responsible.class);
            sessionFactory = config.buildSessionFactory();
            System.out.println("------ Connected to DB -----");
        } catch (Exception e) {
            System.err.println("Error connecting to DB: " + e.getMessage());
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
