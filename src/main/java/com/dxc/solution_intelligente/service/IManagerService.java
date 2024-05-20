package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Admin.*;
import com.dxc.solution_intelligente.DTO.Collaborateur.CollaborateurDTO;
import com.dxc.solution_intelligente.DTO.Manager.*;

import java.util.List;

public interface IManagerService {
    List<ManagerDTO> getAllManagers();
    AddManagerResponse createManager(AddManagerRequest addManagerRequest);
    UpdateManagerResponse updateManager(String username, UpdateManagerRequest updateManagerRequest);
    List<ManagerDTO> findByUsernameContaining(String searchTerm);
}
