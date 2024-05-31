package com.dxc.solution_intelligente.DTO.Tache;

import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateTacheRequest {

    private String Titre;
    private String Description;
    private int Priority;
    private User Responsable;
    private String Statut;
}
