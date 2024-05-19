package com.dxc.solution_intelligente.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    protected String FirstName;
    protected String LastName;
    protected String Username;
    protected String Email;
    protected String Password;
    protected boolean Force_change_password=true;
    protected String Civility;
    protected String Speciality;
    protected int soldeConge=30;
    protected boolean Status=true;
    private String Role;
}
