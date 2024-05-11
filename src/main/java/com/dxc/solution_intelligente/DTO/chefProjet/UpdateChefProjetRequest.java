package com.dxc.solution_intelligente.DTO.chefProjet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateChefProjetRequest {
    private String FirstName;
    private String LastName;
    private String Username;
    private String Email;
    private String Password;
    private String Civility;
    private String Speciality;
    private boolean Status;
    private int soldeConge;
}
