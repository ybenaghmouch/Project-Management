package com.dxc.solution_intelligente.DTO.Tache;

import com.dxc.solution_intelligente.service.model.Collaborateur;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data

public class AddTacheRequest {
    private String code;
    private String Titre;
    private String Description;
    private int Priorité;
    private Collaborateur Responsable=null;
    private String Statut;

    public AddTacheRequest() {
      String code =UUID.randomUUID().toString();
        String[] parts = code.split("-");
        this.code= parts[0];
        System.out.println("Try programiz.pro"+parts[0]);
    }
}
