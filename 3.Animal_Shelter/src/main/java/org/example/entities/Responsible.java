package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "responsibles", schema = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Responsible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String level;

    @OneToMany(mappedBy = "responsible")
    private List<Enclosure> enclosures;

    @Override
    public String toString() {
        return "Id: " + id + ", name: " + firstName + " " + lastName + ", level: " + level;
    }
}
