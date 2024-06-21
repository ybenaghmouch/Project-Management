package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Backlog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titre;
    private String description;
    private String Status;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<UserStory> userStories;
}
