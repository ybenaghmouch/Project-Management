package com.dxc.solution_intelligente.DTO.Sprint;

import com.dxc.solution_intelligente.DTO.Project.ProjectDTO;
import com.dxc.solution_intelligente.service.model.UserStory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AddSprintRequest {
    private String titre;
    private String description;
    private Date date_debut;
    private Date date_fin;
    private String status;
}
