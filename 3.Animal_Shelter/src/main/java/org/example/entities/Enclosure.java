package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "enclosures", schema = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enclosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int number;

    @Column(precision = 2)
    private float square;

    private int capacity;
    private int floor;

    @OneToMany(mappedBy = "enclosure")
    private List<Animal> animals;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private Responsible responsible;

    @Override
    public String toString() {
        return "Id: " + id + ", number: " + number + ", square: " + square +
                " sq.m., capacity: " + capacity + ", floor: " + floor;
    }
}
