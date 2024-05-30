package com.dxc.solution_intelligente.DTO.Role;

import com.dxc.solution_intelligente.DTO.Permission.PermissionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private int id;
    private String authority;
    private List<PermissionDTO> authorities;

}
