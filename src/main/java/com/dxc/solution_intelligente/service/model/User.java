package com.dxc.solution_intelligente.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String FirstName;
    private String LastName;
    private String username;
    private String Email;
    private String Password;
    private boolean Force_change_password;
    @Column(nullable = false)
    private String Civility;
    private String Speciality;
    private boolean Status;
    private double soldeConge;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Role> authorities;
    @ManyToMany(mappedBy = "users")
    //@JsonBackReference
    private List<ChatRoom> chatRooms;


}
