package com.dxc.solution_intelligente.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;

@Data
@NoArgsConstructor
public class UserDTO {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String email;
    protected boolean force_change_password;
    protected String civility;
    protected String speciality;
    protected boolean status;
    private String Role;


}
