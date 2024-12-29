package org.example;

import lombok.*;
import java.sql.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private Date birthDate;
    private String notes;
    private boolean isImportant;
    private Timestamp createdAt;
}
