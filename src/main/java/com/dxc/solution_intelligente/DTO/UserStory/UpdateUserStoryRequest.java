package com.dxc.solution_intelligente.DTO.UserStory;

import com.dxc.solution_intelligente.service.model.Tache;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UpdateUserStoryRequest {

    private String titre;
    private String Description;
    private int Priority;
    private String Statut;
    private List<Tache> Features=null;
}
