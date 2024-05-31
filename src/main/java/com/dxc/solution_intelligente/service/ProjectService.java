package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ProjectRepository;

import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Project;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProjectService implements IProjectService{

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddProjectResponse createProject(AddProjectRequest addProjectRequest) {
        Project bo = modelMapper.map(addProjectRequest, Project.class);
        String name = bo.getNom();
        projectRepository.findProjectByNom(name).ifPresent(a->
                {
                    throw new BusinessException(String.format("Projet deja existe avec le nom [%s]", name));
                });
        AddProjectResponse response = modelMapper.map(projectRepository.save(bo), AddProjectResponse.class);
        response.setMessage(String.format("Projet : [Nom = %s, Description = %s, Date de debut = %s, Date de fin = %s, Duree = %S, Status = %s, Manager = %s, Backlog = %s]", response.getNom(), response.getDescription(), response.getDateDebut(), response.getDateFin(), response.getDuree(), response.getStatus(), response.getManager().getUsername(), response.getBacklogs()));
        return response;
    }

    @Override
    public UpdateProjectResponse updateProject(String name, UpdateProjectRequest updateProjectRequest) {
        Project projectToPersist = modelMapper.map(updateProjectRequest, Project.class);
        Project projectFound = projectRepository.findAll().stream().filter(bo-> bo.getNom() != null && bo.getNom().equals(name)).findFirst().orElseThrow(
                ()-> new BusinessException(String.format("Aucun projet existe avec le nom [%s] ", name))
        );
        projectToPersist.setId(projectFound.getId());
        projectFound.setNom(name);
        UpdateProjectResponse updateProjectResponse = modelMapper.map(projectRepository.save(projectToPersist), UpdateProjectResponse.class);
        updateProjectResponse.setMessage(String.format("Projet avec le nom [%s] a ete modifie avec succes", name));
        return updateProjectResponse;
    }

  
    /*@Override   Correct
    public List<ProjectDTO> findProjectByManager(String username) {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .filter(project -> username.equalsIgnoreCase(project.getManager().getUsername()))
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }*/

    @Override
    public List<ProjectDTO> findProjectByManager(String username) {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> filteredProjects = projects.stream()
                .filter(project -> username.equalsIgnoreCase(project.getManager().getUsername()))
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());

        if (filteredProjects.isEmpty()) {
            throw new BusinessException("Project not found for manager with username: " + username);
        }

        return filteredProjects;
    }



    @Override
    public List<ProjectDTO> findByName(String searchTerm) {
        return projectRepository.findProjectByNom(searchTerm.toLowerCase()).stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class)).collect(Collectors.toList());
    }

}
