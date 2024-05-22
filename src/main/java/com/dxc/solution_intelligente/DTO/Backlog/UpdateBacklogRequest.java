package com.dxc.solution_intelligente.DTO.Backlog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateBacklogRequest {
    private String titre;
    private String description;
    private String Status;
}
