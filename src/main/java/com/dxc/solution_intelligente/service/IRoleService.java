package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Permission.*;
import com.dxc.solution_intelligente.DTO.Role.*;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> getAllRoles();
    AddRoleResponse createRole(AddRoleRequest addRoleRequest);
    UpdateRoleResponse updateRole(String authority, UpdateRoleRequest updateRoleRequest);
}
