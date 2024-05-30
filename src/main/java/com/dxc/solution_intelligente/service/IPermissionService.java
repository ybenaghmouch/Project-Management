package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Permission.*;

import java.util.List;

public interface IPermissionService {
    List<PermissionDTO> getAllPermissions();
    AddPermissionResponse createPermission(AddPermissionRequest addPermissionRequest);
    UpdatePermissionResponse updatePermission(Long id, UpdatePermissionRequest updatePermissionRequest);
}
