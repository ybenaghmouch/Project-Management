package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String FirstName;
    private String LastName;
    private String username;
    private String Email;
    private String Password;
    private boolean Force_change_password;
    private String Civility;
    private String Speciality;
    private boolean Status;
    private int soldeConge;
    @ManyToMany
    private List<Role> authorities;



}
