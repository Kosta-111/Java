package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "animals", schema = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 100)
    private String breed;

    @Column(length = 100)
    private String specie;

    private int age;

    @ManyToOne
    @JoinColumn(name = "enclosure_id")
    private Enclosure enclosure;

    @Override
    public String toString() {
        return "Id: " + id + ", name: " + name + ", age: " + age + ", specie: " + specie + ", breed: " + breed;
    }
}
