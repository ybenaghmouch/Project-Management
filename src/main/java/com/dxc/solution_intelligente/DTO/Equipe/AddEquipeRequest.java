package com.dxc.solution_intelligente.DTO.Equipe;

import com.dxc.solution_intelligente.service.model.ChefProjet;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import com.dxc.solution_intelligente.service.model.Manager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AddEquipeRequest {
    private Long id;
    private String Nom;

    private ChefProjet Chefprojet;

    private List<Collaborateur> Collaborateurs;

    private Manager Manager;
}
