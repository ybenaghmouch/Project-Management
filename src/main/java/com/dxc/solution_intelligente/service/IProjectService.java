package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Project.*;

import java.util.List;

public interface IProjectService {
    List<ProjectDTO> getAllProjects();
    AddProjectResponse createProject(AddProjectRequest addProjectRequest);
    UpdateProjectResponse updateProject(String name, UpdateProjectRequest updateProjectRequest);
    List<ProjectDTO> findProjectByManager(String username);
    List<ProjectDTO> findByName(String searchTerm);
    ProjectDTO findByexactName(String searchTerm);
}
