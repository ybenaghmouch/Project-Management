package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.PermissionRepository;
import com.dxc.solution_intelligente.DTO.Backlog.UpdateBacklogRequest;
import com.dxc.solution_intelligente.DTO.Backlog.UpdateBacklogResponse;
import com.dxc.solution_intelligente.DTO.Manager.*;
import com.dxc.solution_intelligente.DTO.Permission.*;
import com.dxc.solution_intelligente.DTO.Tache.UpdateTacheRequest;
import com.dxc.solution_intelligente.DTO.Tache.UpdateTacheResponse;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Manager;
import com.dxc.solution_intelligente.service.model.Permission;
import com.dxc.solution_intelligente.service.model.Tache;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream().
                map(permission -> modelMapper.map(permission, PermissionDTO.class)).collect(Collectors.toList());
    }

    public AddPermissionResponse createPermission(AddPermissionRequest addPermissionRequest) {
        Permission bo = modelMapper.map(addPermissionRequest, Permission.class);
        String authority = bo.getAuthority();
        permissionRepository.findPermissionByAuthority(authority).ifPresent(a->
        {
            throw new BusinessException(String.format("Permission deja existe [%s]", authority));
        });
        AddPermissionResponse response = modelMapper.map(permissionRepository.save(bo), AddPermissionResponse.class);
        response.setMessage(String.format("Permission : [Authority = %s]", response.getAuthority()));
        return response;
    }



    public UpdatePermissionResponse updatePermission(Long id, UpdatePermissionRequest updatePermissionRequest) {
        Permission permissionToPersist = modelMapper.map(updatePermissionRequest, Permission.class);

        Permission permissionFound = permissionRepository.findAll().stream()
                .filter(bo -> bo.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Permission avec l'id [%s] n'existe pas!", id)));

        permissionToPersist.setId(id);
        //permissionFound.setAuthority(UpdatePermissionRequest.);

        UpdatePermissionResponse updatePermissionResponse = modelMapper.map(permissionRepository.save(permissionToPersist), UpdatePermissionResponse.class);
        updatePermissionResponse.setMessage(String.format("Permission %d a ete modifié avec succes", id));
        return updatePermissionResponse;
    }

}
