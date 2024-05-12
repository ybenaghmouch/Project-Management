package com.dxc.solution_intelligente.DTO.Collaborateur;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollaborateurDTO {

    private Long id;
    private String FirstName;
    private String LastName;
    private String Username;
    private String Email;
    //private String Password;
    private boolean Force_change_password;
    private String Civility;
    private String Speciality;
    private boolean Status;
    private int soldeConge;

}
