package com.example.onlinestoreproject.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phoneNum")
    private String phoneNum = "0";

    @Column(name = "address")
    private String address= "0";

    @Column(name = "postalCode")
    private int postalCode = 0;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Roles> roles;





}
