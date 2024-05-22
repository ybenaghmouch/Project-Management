package com.dxc.solution_intelligente.DTO.Project;

import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Manager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateProjectRequest {
    private String message;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private int duree;
    private String status;
    private Manager manager;
    private List<Backlog> backlogs;
}
