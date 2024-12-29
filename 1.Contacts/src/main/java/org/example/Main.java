package org.example;

import java.sql.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            var dm = new DatabaseManager();
            dm.createTables();
            var scanner = new Scanner(System.in);
            String res = "";

            while(!res.equals("0")) {
                System.out.println("======================");
                System.out.println("[1] - add new contact");
                System.out.println("[2] - show contacts");
                System.out.println("[0] - exit");
                res = scanner.nextLine();

                switch (res) {
                    case "1":
                        var contact = createContact();
                        dm.addContact(contact);
                        break;
                    case "2":
                        dm.showContacts();
                        break;
                    case "0":
                        System.out.println("Bye bye!");
                        dm.closeConnection();
                        break;
                    default:
                        System.out.println("Incorrect choice, try again!");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }

    public static Contact createContact() {
        var scanner = new Scanner(System.in);
        var contact = new Contact();

        System.out.println("Enter first name");
        String temp = scanner.nextLine().trim();
        contact.setFirstName(temp);

        System.out.println("Enter last name");
        temp = scanner.nextLine().trim();
        contact.setLastName(temp);

        System.out.println("Enter birth date: yyyy-mm-dd");
        temp = scanner.nextLine().trim();
        contact.setBirthDate(Date.valueOf(temp));

        System.out.println("Enter phone number");
        temp = scanner.nextLine().trim();
        contact.setPhone(temp);

        System.out.println("Enter notes");
        temp = scanner.nextLine().trim();
        contact.setNotes(temp);

        System.out.println("Is important (y/n)?");
        temp = scanner.nextLine().trim().toLowerCase();
        contact.setImportant(temp.equals("y"));
        return contact;
    }
}