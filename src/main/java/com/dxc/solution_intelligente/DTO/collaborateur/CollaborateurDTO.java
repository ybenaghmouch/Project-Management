package com.dxc.solution_intelligente.DTO.collaborateur;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollaborateurDTO {

    protected Long id;
    protected String FirstName;
    protected String LastName;
    protected String Username;
    protected String Email;
    protected String Password;
    protected boolean Force_change_password;
    protected String Civility;
    protected String Speciality;
    protected boolean Status;
}
