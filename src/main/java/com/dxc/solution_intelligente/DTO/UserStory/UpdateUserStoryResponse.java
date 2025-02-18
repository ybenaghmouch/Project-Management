package com.dxc.solution_intelligente.DTO.UserStory;

import com.dxc.solution_intelligente.DTO.Tache.TacheDTO;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.Tache;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UpdateUserStoryResponse {
    private long Id;
    private String code;
    private String titre;
    private String Description;
    private int Priority;
    private String Statut;
    private List<TacheDTO> Features;
    private String message;
    private UserDTO Responsable;
}
