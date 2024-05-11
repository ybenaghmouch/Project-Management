package com.dxc.solution_intelligente.DTO.Manager;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private boolean force_change_password;
    private String civility;
    private String speciality;
    private boolean status;

}
