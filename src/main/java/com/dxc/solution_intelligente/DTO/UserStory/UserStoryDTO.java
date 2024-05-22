package com.dxc.solution_intelligente.DTO.UserStory;

import com.dxc.solution_intelligente.service.model.Tache;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UserStoryDTO {
    private long Id;
    private String code;
    private String titre;
    private String Description;
    private int Priority;
    private String Statut;
    private List<Tache> Features;
}
