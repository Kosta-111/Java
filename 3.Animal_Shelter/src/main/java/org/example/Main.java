package org.example;

import org.example.entities.Animal;
import org.example.entities.Enclosure;
import org.example.entities.Responsible;
import org.example.util.HibernateUtil;
import java.util.Random;

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
            var random = new Random();
            String[] species = {"Cat", "Dog", "Rabbit"};
            String[] breeds = {"Siamese", "Bulldog", "Mini", "Maxi", "Pushok"};

            for (int i = 0; i < 3; i++) {
                var responsible = new Responsible();
                responsible.setFirstName("F" + i);
                responsible.setLastName("L" + i);
                responsible.setLevel("Level" + i);
                session.persist(responsible);

                for (int j = 0; j < 2; j++) {
                    var enclosure = new Enclosure();
                    enclosure.setCapacity(j + 2);
                    enclosure.setFloor(j + i);
                    enclosure.setNumber(j * 100 + j);
                    enclosure.setSquare(j + (float)j/10);
                    enclosure.setResponsible(responsible);
                    session.persist(enclosure);

                    for (int k = 0; k < 2; k++) {
                        var animal = new Animal();
                        animal.setAge(k + 2);
                        animal.setName("name" + k);
                        animal.setSpecie(species[random.nextInt(species.length)]);
                        animal.setBreed(breeds[random.nextInt(breeds.length)]);
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
                var enclosure = animal.getEnclosure();
                System.out.println("Animal Enclosure - " + enclosure);
                System.out.println("Animal Responsible - " + enclosure.getResponsible());
                System.out.println("===========================================");
            }
        } catch (Exception e) {
            System.err.println("Error loading animals: " + e.getMessage());
        }
    }
}