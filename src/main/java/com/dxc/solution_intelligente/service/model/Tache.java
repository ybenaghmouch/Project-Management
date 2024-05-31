package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String code;
    private String titre;
    private String Description;
    private int Priority;
    @ManyToOne
    private User Responsable;
    private String Statut;


}
