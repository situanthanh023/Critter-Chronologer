package com.udacity.jdnd.course3.critter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id=0;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @Nationalized
    private String name;

    private LocalDate birthDate;

    @Column(length = 300)
    private String notes;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer owner;
}
