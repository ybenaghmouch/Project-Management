package com.dxc.solution_intelligente.DTO.Equipe;

import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEquipeRequest {
    private String Nom;

    private UserDTO Chefprojet;

    private List<UserDTO> Collaborateurs;

    private UserDTO Manager;
}
