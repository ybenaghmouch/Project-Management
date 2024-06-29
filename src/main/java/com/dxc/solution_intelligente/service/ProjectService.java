package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.EquipeRepository;
import com.dxc.solution_intelligente.DAO.ProjectRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.SprintRepository;
import com.dxc.solution_intelligente.DTO.Backlog.BacklogDTO;
import com.dxc.solution_intelligente.DTO.Equipe.EquipeDTO;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.DTO.Sprint.SprintDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final SprintRepository sprintRepository;
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
        projectRepository.findProjectByNom(addProjectRequest.getNom()).ifPresent(a -> {
            throw new BusinessException(String.format("Projet deja existe avec le nom [%s]", addProjectRequest.getNom()));
        });

        User manager = userRepository.findById(addProjectRequest.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));

        Equipe equipe = equipeRepository.findById(addProjectRequest.getEquipe().getId())
                .orElseThrow(() -> new BusinessException("Equipe not found"));

        Project project = new Project();
        project.setNom(addProjectRequest.getNom());
        project.setDescription(addProjectRequest.getDescription());
        project.setDateDebut(addProjectRequest.getDateDebut());
        project.setDateFin(addProjectRequest.getDateFin());
        project.setDuree(addProjectRequest.getDuree());
        project.setStatus(addProjectRequest.getStatus());
        project.setManager(manager);
        project.setEquipe(equipe);
        project.setBacklogs(null); // Always set to null in creation
        project.setSprints(null);  // Always set to null in creation

        Project savedProject = projectRepository.save(project);

        AddProjectResponse response = new AddProjectResponse();
      //  response.setId(savedProject.getId());
        response.setNom(savedProject.getNom());
        response.setDescription(savedProject.getDescription());
        response.setDateDebut(savedProject.getDateDebut());
        response.setDateFin(savedProject.getDateFin());
        response.setDuree(savedProject.getDuree());
        response.setStatus(savedProject.getStatus());

        UserDTO managerDTO = modelMapper.map(manager, UserDTO.class);
        response.setManager(managerDTO);

        EquipeDTO equipeDTO = new EquipeDTO();
        equipeDTO.setId(equipe.getId());
        equipeDTO.setNom(equipe.getNom());
        response.setEquipe(equipeDTO);

        response.setBacklogs(null); // Always set to null in creation response
        response.setSprints(null);  // Always set to null in creation response

        response.setMessage(String.format("Projet : [Nom = %s, Description = %s, Date de debut = %s, Date de fin = %s, Duree = %S, Status = %s, Manager = %s, Equipe = %s]",
                response.getNom(), response.getDescription(), response.getDateDebut(), response.getDateFin(), response.getDuree(), response.getStatus(), response.getManager().getUsername(), response.getEquipe()));

        return response;
    }

    @Override
    public UpdateProjectResponse updateProject(String name, UpdateProjectRequest updateProjectRequest) {
        Project projectFound = projectRepository.findAll().stream()
                .filter(bo -> bo.getNom() != null && bo.getNom().equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s] ", name)));

        User manager = userRepository.findById(updateProjectRequest.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        Equipe equipe = equipeRepository.findById(updateProjectRequest.getEquipe().getId())
                .orElseThrow(() -> new BusinessException("Equipe not found"));

        List<Backlog> backlogs = projectFound.getBacklogs();
        if (updateProjectRequest.getBacklogs() != null) {
            backlogs = backlogRepository.findAllById(
                    updateProjectRequest.getBacklogs().stream()
                            .map(BacklogDTO::getId)
                            .collect(Collectors.toList())
            );
            if (backlogs.size() != updateProjectRequest.getBacklogs().size()) {
                throw new BusinessException("One or more backlogs not found");
            }
        }

        List<Sprint> sprints = projectFound.getSprints();
        if (updateProjectRequest.getSprints() != null) {
            sprints = sprintRepository.findAllById(
                    updateProjectRequest.getSprints().stream()
                            .map(SprintDTO::getId)
                            .collect(Collectors.toList())
            );
            if (sprints.size() != updateProjectRequest.getSprints().size()) {
                throw new BusinessException("One or more sprints not found");
            }
        }

        projectFound.setNom(updateProjectRequest.getNom());
        projectFound.setDescription(updateProjectRequest.getDescription());
        projectFound.setDateDebut(updateProjectRequest.getDateDebut());
        projectFound.setDateFin(updateProjectRequest.getDateFin());
        projectFound.setDuree(updateProjectRequest.getDuree());
        projectFound.setStatus(updateProjectRequest.getStatus());
        projectFound.setManager(manager);
        projectFound.setEquipe(equipe);
        projectFound.setBacklogs(backlogs);
        projectFound.setSprints(sprints);

        Project savedProject = projectRepository.save(projectFound);

        UpdateProjectResponse updateProjectResponse = new UpdateProjectResponse();
      //  updateProjectResponse.setId(savedProject.getId());
        updateProjectResponse.setNom(savedProject.getNom());
        updateProjectResponse.setDescription(savedProject.getDescription());
        updateProjectResponse.setDateDebut(savedProject.getDateDebut());
        updateProjectResponse.setDateFin(savedProject.getDateFin());
        updateProjectResponse.setDuree(savedProject.getDuree());
        updateProjectResponse.setStatus(savedProject.getStatus());

        UserDTO managerDTO = modelMapper.map(manager, UserDTO.class);
        updateProjectResponse.setManager(managerDTO);

        EquipeDTO equipeDTO = new EquipeDTO();
        equipeDTO.setId(equipe.getId());
        equipeDTO.setNom(equipe.getNom());
        updateProjectResponse.setEquipe(equipeDTO);

        updateProjectResponse.setBacklogs(backlogs != null ? backlogs.stream().map(backlog -> {
            BacklogDTO backlogDTO = new BacklogDTO();
            backlogDTO.setId(backlog.getId());
            backlogDTO.setTitre(backlog.getTitre());
            return backlogDTO;
        }).collect(Collectors.toList()) : null);

        updateProjectResponse.setSprints(sprints != null ? sprints.stream().map(sprint -> {
            SprintDTO sprintDTO = new SprintDTO();
            sprintDTO.setId(sprint.getId());
            sprintDTO.setTitre(sprint.getTitre());
            return sprintDTO;
        }).collect(Collectors.toList()) : null);

        updateProjectResponse.setMessage(String.format("Projet avec le nom [%s] a été modifié avec succès", name));

        return updateProjectResponse;
    }



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
        return modelMapper.map(projectRepository.findProjectByNom(searchTerm.toLowerCase()), ProjectDTO.class);
    }

    @Override
    public String deleteProjectById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity sprint");
        Project projectFound = projectRepository.findAll().stream()
                .filter(project -> project.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("No Project with identity %d exist in database", id)));
        projectRepository.delete(projectFound);
        return String.format("Project with identity %d is deleted with success", id);
    }
}
