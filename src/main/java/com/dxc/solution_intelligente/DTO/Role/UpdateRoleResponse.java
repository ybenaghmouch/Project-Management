package com.dxc.solution_intelligente.DTO.Role;

import com.dxc.solution_intelligente.DTO.Permission.PermissionDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateRoleResponse {
    private String authority;
    private List<PermissionDTO> authorities;
    private String message;
}
