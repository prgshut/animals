package ru.artplan.animals.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "kind")
public class Kind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
