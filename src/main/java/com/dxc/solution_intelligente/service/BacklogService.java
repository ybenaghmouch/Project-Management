package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.ProjectRepository;
import com.dxc.solution_intelligente.DTO.Backlog.*;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Permission;
import com.dxc.solution_intelligente.service.model.Project;
import com.dxc.solution_intelligente.service.model.UserStory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class BacklogService implements IBacklogService{
    private final BacklogRepository backlogRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    @Override
    public List<BacklogDTO> getAllBacklogs() {
        return backlogRepository.findAll().stream()
                .map(backlog -> modelMapper.map(backlog, BacklogDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddBacklogResponse createBacklog(AddBacklogRequest addBacklogRequest) {
        Backlog bo = modelMapper.map(addBacklogRequest, Backlog.class);
        String titre = bo.getTitre();
        backlogRepository.findBacklogByTitre(titre).ifPresent(backlog -> {
            throw new BusinessException(String.format("Backlog avec le titre [%s] déjà existe", titre));
        });
        AddBacklogResponse response = modelMapper.map(backlogRepository.save(bo), AddBacklogResponse.class);
        response.setMessage(String.format("Backlog : [Titre = %s, Description = %s, Status = %s]", response.getTitre(), response.getDescription(), response.getStatus()));
        return response;
    }

    public AddBacklogResponse addBacklogToProject(String projectName, AddBacklogRequest addBacklogRequest) {
        // Create the backlog
        Backlog bo = modelMapper.map(addBacklogRequest, Backlog.class);
        String titre = bo.getTitre();
        backlogRepository.findBacklogByTitre(titre).ifPresent(backlog -> {
            throw new BusinessException(String.format("Backlog avec le titre [%s] déjà existe", titre));
        });

        // Save the backlog first
        Backlog savedBacklog = backlogRepository.save(bo);

        // Find the project by name
        Project project = projectRepository.findAll().stream()
                .filter(proj -> proj.getNom() != null && proj.getNom().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s] ", projectName)));

        // Add the saved backlog to the project
        project.getBacklogs().add(savedBacklog);
        projectRepository.save(project);

        // Prepare the response
        AddBacklogResponse response = modelMapper.map(savedBacklog, AddBacklogResponse.class);
        response.setMessage(String.format("Backlog ajouté avec succès au projet [%s] : [Titre = %s, Description = %s, Status = %s]", projectName, response.getTitre(), response.getDescription(), response.getStatus()));

        return response;
    }

    @Override
    public UpdateBacklogResponse updateBacklog(String titre, UpdateBacklogRequest updateBacklogRequest) {
        Backlog backlogFound = backlogRepository.findAll().stream()
                .filter(bo -> bo.getTitre() != null && bo.getTitre().equals(titre))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun backlog existe avec le titre [%s] ", titre)));

        Backlog backlogToPersist = modelMapper.map(updateBacklogRequest, Backlog.class);
        backlogToPersist.setId(backlogFound.getId());
        backlogToPersist.setTitre(titre);

        // Handle existing user stories
        List<UserStory> existingUserStories = backlogFound.getUserStories();
        backlogToPersist.setUserStories(existingUserStories);

        Backlog updatedBacklog = backlogRepository.save(backlogToPersist);

        UpdateBacklogResponse updateBacklogResponse = modelMapper.map(updatedBacklog, UpdateBacklogResponse.class);
        updateBacklogResponse.setMessage(String.format("Backlog avec le titre [%s] a été modifié avec succès", titre));

        return updateBacklogResponse;
    }


    @Override
    public List<BacklogDTO> findByTitre(String titre) {
        return backlogRepository.findByTitreContainingIgnoreCase(titre.toLowerCase()).stream()
                .map(backlog -> modelMapper.map(backlog, BacklogDTO.class)).collect(Collectors.toList());
    }
    @Override
    public BacklogDTO searchByTitre(String titre) {
        return
                modelMapper.map(backlogRepository.findBacklogByTitre(titre.toLowerCase()), BacklogDTO.class);
    }

    @Override
    public String deleteBacklogById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity backlog");
        Backlog backlogFound = backlogRepository.findAll().stream().filter(backlog -> backlog.getId()==id).findFirst().orElseThrow(
                () -> new BusinessException(String.format("No customer with identity %d exist in database", id))
        );

        backlogRepository.delete(backlogFound);
        return String.format("Backlog with identity %d is deleted with success", id);

    }


}
