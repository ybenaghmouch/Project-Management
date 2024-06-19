package com.dxc.solution_intelligente.DTO.Project;

import com.dxc.solution_intelligente.DTO.Backlog.BacklogDTO;
import com.dxc.solution_intelligente.DTO.Sprint.SprintDTO;
import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.User;
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
    private User manager;
    private List<Backlog> backlogs;
    private List<SprintDTO> sprints;
}
