package com.dxc.solution_intelligente.DTO.Permission;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePermissionResponse {
    private String authority;
    private String message;
}
