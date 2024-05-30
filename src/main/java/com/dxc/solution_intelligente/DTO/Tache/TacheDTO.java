package com.dxc.solution_intelligente.DTO.Tache;

import com.dxc.solution_intelligente.DTO.Collaborateur.CollaborateurDTO;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TacheDTO {
    private Long Id;
    private String Code;
    private String Titre;
    private String Description;
    private int Priority;
    private Collaborateur Responsable;
    private String Statut;
}
