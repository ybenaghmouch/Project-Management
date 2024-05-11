package com.dxc.solution_intelligente.DTO.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddAdminRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private boolean force_change_password = true;
    private String civility;
    private String speciality;
    private boolean status = true;
}
