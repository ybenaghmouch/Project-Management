package com.dxc.solution_intelligente.DTO.Equipe;

import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UpdateEquipeRequest {
    private Long id;
    private String Nom;

    private User Chefprojet;

    private List<User> Collaborateurs;

    private User Manager;
}
