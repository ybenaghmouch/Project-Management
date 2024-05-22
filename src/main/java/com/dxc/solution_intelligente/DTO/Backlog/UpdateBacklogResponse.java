package com.dxc.solution_intelligente.DTO.Backlog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateBacklogResponse {
    private String message;
    private String titre;
    private String description;
    private String Status;
}
