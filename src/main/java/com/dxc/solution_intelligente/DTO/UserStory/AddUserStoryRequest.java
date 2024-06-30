package com.dxc.solution_intelligente.DTO.UserStory;

import com.dxc.solution_intelligente.DTO.Tache.TacheDTO;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.Tache;
import com.dxc.solution_intelligente.service.model.User;
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
    private List<TacheDTO> Features=null;
    private UserDTO Responsable;
    public AddUserStoryRequest() {
        String code = UUID.randomUUID().toString();
        String[] parts = code.split("-");
        this.code= parts[0];
        System.out.println("Try programiz.pro"+parts[0]);
    }
}
