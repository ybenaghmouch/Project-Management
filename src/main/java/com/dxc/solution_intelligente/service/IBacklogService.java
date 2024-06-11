package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Backlog.*;
import com.dxc.solution_intelligente.DTO.Project.ProjectDTO;

import java.util.List;

public interface IBacklogService {
    List<BacklogDTO> getAllBacklogs();
    AddBacklogResponse createBacklog(AddBacklogRequest addBacklogRequest);
    UpdateBacklogResponse updateBacklog(String titre, UpdateBacklogRequest updateBacklogRequest);
    List<BacklogDTO> findByTitre(String titre);
    String deleteBacklogById(Long id);

}
