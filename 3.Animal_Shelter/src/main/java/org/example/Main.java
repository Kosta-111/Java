package org.example;

import com.github.javafaker.Faker;
import org.example.entities.Animal;
import org.example.entities.Enclosure;
import org.example.entities.Responsible;
import org.example.util.HibernateUtil;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        try {
            HibernateUtil.connect();
            fillRandomData();
            showAnimals();
        } catch (Exception e) {
            System.err.println("Something went wrong: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
        }
    }

    public static void fillRandomData() {
        try (var session = HibernateUtil.getSession()) {
            var transaction = session.beginTransaction();

            if (!session.createQuery("from Animal", Animal.class).getResultList().isEmpty()
                || !session.createQuery("from Enclosure", Enclosure.class).getResultList().isEmpty()
                || !session.createQuery("from Responsible", Responsible.class).getResultList().isEmpty()
            ) {
                System.out.println("===== Table data already exists! =====");
                return;
            }
            var faker = new Faker(new Locale("en-US"));

            for (int i = 0; i < 3; i++) {
                var responsible = new Responsible();
                responsible.setFirstName(faker.name().firstName());
                responsible.setLastName(faker.name().lastName());
                responsible.setPhone(faker.phoneNumber().cellPhone());
                session.persist(responsible);

                var count = faker.number().numberBetween(2, 5);
                for (int j = 0; j < count; j++) {
                    var enclosure = new Enclosure();
                    enclosure.setCapacity(faker.number().numberBetween(2, 5));
                    enclosure.setFloor(faker.number().numberBetween(1, 3));
                    enclosure.setNumber(enclosure.getFloor() * 100 + faker.number().numberBetween(1, 100));
                    enclosure.setSquare((float)faker.number().numberBetween(20, 100) / 10);
                    enclosure.setResponsible(responsible);
                    session.persist(enclosure);

                    count = faker.number().numberBetween(1, enclosure.getCapacity() + 1);
                    var isCats = faker.bool().bool();
                    for (int k = 0; k < count; k++) {
                        var animal = new Animal();
                        animal.setAge(faker.number().numberBetween(1, 11));
                        animal.setSpecie(isCats ? "Cat" : "Dog");
                        animal.setName(isCats ? faker.cat().name() : faker.dog().name());
                        animal.setBreed(isCats ? faker.cat().breed() : faker.dog().breed());
                        animal.setEnclosure(enclosure);
                        session.persist(animal);
                    }
                }
            }
            transaction.commit();
            System.out.println("===== Random data created! =====");
        } catch (Exception e) {
            System.err.println("Error creating random data: " + e.getMessage());
        }
    }

    public static void showAnimals() {
        try (var session = HibernateUtil.getSession()) {
            var transaction = session.beginTransaction();
            var query = session.createQuery("FROM Animal", Animal.class);
            var animals = query.getResultList();
            transaction.commit();
            System.out.println("\nAnimals:");
            for (var animal : animals) {
                System.out.println("Animal - " + animal);
                System.out.println("Animal Enclosure - " + animal.getEnclosure());
                System.out.println("Animal Responsible - " + animal.getEnclosure().getResponsible());
                System.out.println("===========================================");
            }
        } catch (Exception e) {
            System.err.println("Error loading animals: " + e.getMessage());
        }
    }
}