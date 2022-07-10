package ru.artplan.animals.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kind_id")
    private Kind kind;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "gender")
    private char gender;
    @Column(name = "nickname")
    private String nickName;

    @ManyToOne
    @JoinTable(name = "animal_user",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User owner;
}
