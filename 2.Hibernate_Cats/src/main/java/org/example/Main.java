package org.example;

import org.example.entities.Cat;
import org.example.util.HibernateUtil;
import java.util.Scanner;

public class Main {
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            HibernateUtil.connect();
            scanner = new Scanner(System.in);
            String res = "";
            while (!res.equals("0")) {
                System.out.println("====== Menu ======");
                System.out.println("[1] - create cat");
                System.out.println("[2] - edit cat");
                System.out.println("[3] - delete cat");
                System.out.println("[4] - show cats");
                System.out.println("[0] - exit");
                res = scanner.nextLine().trim();

                switch (res) {
                    case "1":
                        createCat();
                        break;
                    case "2":
                        editCat();
                        break;
                    case "3":
                        deleteCat();
                        break;
                    case "4":
                        showAllCats();
                        break;
                    case "0":
                        System.out.println("Bye bye!");
                        break;
                    default:
                        System.out.println("Incorrect choice, try again!");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Something went wrong: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
        }
    }

    public static void deleteCat() {
        try (var session = HibernateUtil.getSession()) {
            System.out.println("Enter removed cat id");
            String temp = scanner.nextLine().trim();
            var id = Integer.parseInt(temp);

            var transaction = session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            if (cat != null) {
                session.remove(cat);
            }
            transaction.commit();
            System.out.println(cat != null ? "Cat deleted!" : "Cat not found!");
        } catch (Exception e) {
            System.err.println("Error deleting cat: " + e.getMessage());
        }
    }

    public static void showAllCats() {
        try (var session = HibernateUtil.getSession()) {
            var transaction = session.beginTransaction();
            var query = session.createQuery("FROM Cat", Cat.class);
            var cats = query.getResultList();
            transaction.commit();
            for (var cat : cats) {
                System.out.println(cat);
            }
        } catch (Exception e) {
            System.err.println("Error loading cats: " + e.getMessage());
        }
    }

    public static void editCat() {
        try (var session = HibernateUtil.getSession()) {
            System.out.println("Enter edited cat id");
            String temp = scanner.nextLine().trim();
            var id = Integer.parseInt(temp);

            var transaction = session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            if (cat != null) {
                var edited = getCatFromUser();
                edited.setId(id);
                session.merge(edited);
            }
            transaction.commit();
            System.out.println(cat != null ? "Cat edited!" : "Cat not found!");
        } catch (Exception e) {
            System.err.println("Error editing cat: " + e.getMessage());
        }
    }

    public static void createCat() {
        try (var session = HibernateUtil.getSession()) {
            Cat cat = getCatFromUser();
            var transaction = session.beginTransaction();
            session.persist(cat);
            transaction.commit();
            System.out.println("Cat created!");
        } catch (Exception e) {
            System.err.println("Error creating cat: " + e.getMessage());
        }
    }

    private static Cat getCatFromUser() {
        Cat cat = new Cat();
        System.out.println("Enter cat name");
        String temp = scanner.nextLine().trim();
        cat.setName(temp);

        System.out.println("Enter cat age");
        temp = scanner.nextLine().trim();
        cat.setAge(Integer.parseInt(temp));

        System.out.println("Enter cat breed");
        temp = scanner.nextLine().trim();
        cat.setBreed(temp);
        return cat;
    }
}