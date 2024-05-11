package com.dxc.solution_intelligente.DTO.collaborateur;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCollaborateurRequest {
    private String FirstName;
    private String LastName;
    private String Username;
    private String Email;
    private String Password;
    private boolean Force_change_password=true;
    private String Civility;
    private String Speciality;
    private int soldeConge=30;
    private boolean Status=true;
}
