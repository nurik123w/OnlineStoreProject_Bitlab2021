package com.example.onlinestoreproject.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "categories",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Item> itemList = new ArrayList<>();


}
