package com.dxc.solution_intelligente.DTO.Equipe;

import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AddEquipeResponse {
    private Long id;
    private String Nom;

    private UserDTO Chefprojet;

    private List<UserDTO> Collaborateurs;

    private UserDTO Manager;
    private String message;
}
