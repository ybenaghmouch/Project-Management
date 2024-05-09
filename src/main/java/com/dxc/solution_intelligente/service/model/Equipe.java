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
    private String Nom;
    @ManyToOne
    private ChefProjet Chefprojet;
    @ManyToMany
    private List<Collaborateur> Collaborateurs;
    @ManyToOne
    private Manager Manager;


}
