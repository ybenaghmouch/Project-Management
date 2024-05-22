package com.dxc.solution_intelligente.DTO.UserStory;

import com.dxc.solution_intelligente.service.model.Tache;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class AddUserStoryRequest {

    private String code;
    private String titre;
    private String Description;
    private int Priority;
    private String Statut;
    private List<Tache> Features=null;
    public AddUserStoryRequest() {
        String code = UUID.randomUUID().toString();
        String[] parts = code.split("-");
        this.code= parts[0];
        System.out.println("Try programiz.pro"+parts[0]);
    }
}
