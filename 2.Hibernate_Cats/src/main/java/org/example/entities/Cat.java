package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cats", schema = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 100)
    private String breed;

    private int age;

    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", age: " + age + ", breed: " + breed;
    }
}
