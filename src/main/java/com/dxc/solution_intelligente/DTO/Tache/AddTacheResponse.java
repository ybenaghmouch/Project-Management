package com.dxc.solution_intelligente.DTO.Tache;

import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddTacheResponse {
    private Long Id;
    private String Code;
    private String Titre;
    private String Description;
    private int Priority;
    private UserDTO Responsable;
    private String Statut;
    private String message;
}
