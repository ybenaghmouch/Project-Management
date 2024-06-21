package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private int duree;
    private String status;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User manager;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Backlog> backlogs;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Sprint> sprints;

}
