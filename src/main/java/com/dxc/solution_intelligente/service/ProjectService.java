package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.EquipeRepository;
import com.dxc.solution_intelligente.DAO.ProjectRepository;

import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Equipe.EquipeDTO;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class ProjectService implements IProjectService{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final EquipeRepository equipeRepository;
    @Autowired
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

        Equipe equipe =  equipeRepository.findById(response.getEquipe().getId()).orElseThrow(() -> new BusinessException("Equipe not found"));
        response.setEquipe(equipe);
        User manager = userRepository.findById(response.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        response.setManager(manager);
        response.setMessage(String.format("Projet : [Nom = %s, Description = %s, Date de debut = %s, Date de fin = %s, Duree = %S, Status = %s, Manager = %s, Backlog = %s, Equipe = %s]", response.getNom(), response.getDescription(), response.getDateDebut(), response.getDateFin(), response.getDuree(), response.getStatus(), response.getManager().getUsername(), response.getBacklogs(), response.getEquipe()));

        return response;
    }

    @Override
    public UpdateProjectResponse updateProject(String name, UpdateProjectRequest updateProjectRequest) {
        User manager = userRepository.findById(updateProjectRequest.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        updateProjectRequest.setManager(manager);

        Equipe equipe =  equipeRepository.findById(updateProjectRequest.getEquipe().getId()).orElseThrow(() -> new BusinessException("Equipe not found"));
        updateProjectRequest.setEquipe(equipe);

        Project projectFound = projectRepository.findAll().stream()
                .filter(bo -> bo.getNom() != null && bo.getNom().equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s] ", name)));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        // Utiliser ModelMapper pour mapper les propriétés non-nulles
        modelMapper.map(updateProjectRequest, projectFound);


        // Traiter les backlogs

        Project savedProject = projectRepository.save(projectFound);

        UpdateProjectResponse updateProjectResponse = modelMapper.map(savedProject, UpdateProjectResponse.class);
        updateProjectResponse.setMessage(String.format("Projet avec le nom [%s] a été modifié avec succès", name));

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
        return projectRepository.findByNomContainingIgnoreCase(searchTerm.toLowerCase()).stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO findByexactName(String searchTerm) {
       return  modelMapper.map(projectRepository.findProjectByNom(searchTerm.toLowerCase()), ProjectDTO.class);
    }

    @Override
    public String deleteProjectById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity sprint");
        Project projectFound = projectRepository.findAll().stream().filter(sprint -> sprint.getId()==id).findFirst().orElseThrow(
                () -> new BusinessException(String.format("No Project with identity %d exist in database", id))
        );
        projectRepository.delete(projectFound);
        return String.format("Project with identity %d is deleted with success", id);
    }

}
