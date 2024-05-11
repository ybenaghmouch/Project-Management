package com.dxc.solution_intelligente.DTO.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateAdminResponse {
    private String message;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String civility;
    private String speciality;
    private boolean status;
}
