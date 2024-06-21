package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User Chefprojet;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> Collaborateurs;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User Manager;


}
