package com.dxc.solution_intelligente.DTO.Conge;

import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AddCongeResponse {
    private UserDTO Demandeur;
    private String motif;
    private UserDTO Backup;
    private int Duration;
    private Date FromDate;
    private Date EndDate;
    private String Status;
    private String message;
}
