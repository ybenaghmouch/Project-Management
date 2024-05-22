package com.dxc.solution_intelligente.service.model;

import com.dxc.solution_intelligente.service.model.Tache;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String code;
    private String titre;
    private String Description;
    private int Priority;
    private String Statut;
    @OneToMany
    private List<Tache> Features;
}
