package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class DatabaseManager {
    Connection connection;

    public DatabaseManager() {
        try {
            var objectMapper = new ObjectMapper();
            var fileName = getClass().getClassLoader()
                    .getResource("option.json").toURI();
            Path filePath = Path.of(fileName);
            String jsonStr = Files.readString(filePath);
            Config config = objectMapper.readValue(jsonStr, Config.class);

            String url = "jdbc:postgresql://" + config.getHost() + ":5432/" + config.getNameDb();
            connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
            System.out.println("------Connected to db------");
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.err.println("File access error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Reading file error: " + e.getMessage());
        }
    }

    public void createTables() {
        create_contacts();
    }
    private void create_contacts() {
        try {
            var fileName = getClass().getClassLoader()
                    .getResource("contacts.sql").toURI();
            Path filePath = Path.of(fileName);
            String sql = Files.readString(filePath);

            Statement command = connection.createStatement();
            command.executeUpdate(sql);
            command.close();
        } catch (URISyntaxException e) {
            System.err.println("File access error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Reading file error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("CREATE TABLE command error: " + e.getMessage());
        }
    }

    public void addContact(Contact contact) {
        String sql = "INSERT INTO users.contacts (first_name, last_name, phone, birth_date, notes, is_important) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, contact.getFirstName());
            preparedStatement.setString(2, contact.getLastName());
            preparedStatement.setString(3, contact.getPhone());
            preparedStatement.setDate(4, contact.getBirthDate());
            preparedStatement.setString(5, contact.getNotes());
            preparedStatement.setBoolean(6, contact.isImportant());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New contact inserted!");
            }
        } catch (SQLException e) {
            System.err.println("INSERT command error: " + e.getMessage());
        }
    }

    public void showContacts() {
        try {
            String sql = "SELECT * FROM users.contacts";
            var command = connection.createStatement();
            var res = command.executeQuery(sql);
            while (res.next()) {
                var contact = new Contact();
                contact.setId(res.getInt("id"));
                contact.setFirstName(res.getString("first_name"));
                contact.setLastName(res.getString("last_name"));
                contact.setBirthDate(res.getDate("birth_date"));
                contact.setPhone(res.getString("phone"));
                contact.setImportant(res.getBoolean("is_important"));
                contact.setNotes(res.getString("notes"));
                contact.setCreatedAt(res.getTimestamp("created_at"));
                System.out.println(contact);
            }
            res.close();
            command.close();
        } catch (SQLException e) {
            System.err.println("SELECT command error: : " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (!connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection closing error: " + e.getMessage());
        }
    }
}
