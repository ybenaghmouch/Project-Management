package com.dxc.solution_intelligente.DTO.collaborateur;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCollaborateurResponse {
    private String FirstName;
    private String LastName;
    private String Username;
    private String Email;
    private String Civility;
    private String Speciality;
    private boolean Status;
    private String message;
    private int soldeConge;

}

