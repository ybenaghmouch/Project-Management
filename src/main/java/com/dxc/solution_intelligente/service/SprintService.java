package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ProjectRepository;
import com.dxc.solution_intelligente.DAO.SprintRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.Sprint.SprintDTO;
import com.dxc.solution_intelligente.DTO.Sprint.UpdateSprintRequest;
import com.dxc.solution_intelligente.DTO.Sprint.UpdateSprintResponse;
import com.dxc.solution_intelligente.DTO.Sprint.AddSprintRequest;
import com.dxc.solution_intelligente.DTO.Sprint.AddSprintResponse;
import com.dxc.solution_intelligente.DTO.UserStory.UserStoryDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Project;
import com.dxc.solution_intelligente.service.model.Sprint;

import com.dxc.solution_intelligente.service.model.UserStory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class SprintService implements ISprintService{
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<SprintDTO> getAllSprints() {
        return sprintRepository.findAll().stream()
                .map(sprint -> modelMapper.map(sprint, SprintDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddSprintResponse createSprint(AddSprintRequest addSprintRequest) {
        String titre = addSprintRequest.getTitre();
        sprintRepository.findSprintByTitre(titre).ifPresent(sprint -> {
            throw new BusinessException(String.format("Sprint avec le titre [%s] déjà existe", titre));
        });

        Sprint sprint = new Sprint();
        sprint.setTitre(addSprintRequest.getTitre());
        sprint.setDescription(addSprintRequest.getDescription());
        sprint.setDate_debut(addSprintRequest.getDate_debut());
        sprint.setDate_fin(addSprintRequest.getDate_fin());
        sprint.setStatus(addSprintRequest.getStatus());
        sprint.setUserStories(null);

        Sprint savedSprint = sprintRepository.save(sprint);

        AddSprintResponse response = new AddSprintResponse();
      //  response.setId(savedSprint.getId());
        response.setTitre(savedSprint.getTitre());
        response.setDescription(savedSprint.getDescription());
        response.setDate_debut(savedSprint.getDate_debut());
        response.setDate_fin(savedSprint.getDate_fin());
        response.setStatus(savedSprint.getStatus());
        response.setUserStories(null);

        response.setMessage(String.format("Sprint : [Titre = %s, Description = %s, Status = %s, Date de debut = %s, Date de fin = %s]",
                response.getTitre(), response.getDescription(), response.getStatus(), response.getDate_debut(), response.getDate_fin()));

        return response;
    }

    @Override
    public AddSprintResponse addSprintToProject(String projectName, AddSprintRequest addSprintRequest) {
        String titre = addSprintRequest.getTitre();
        sprintRepository.findSprintByTitre(titre).ifPresent(sprint -> {
            throw new BusinessException(String.format("Sprint avec le titre [%s] déjà existe", titre));
        });

        List<UserStory> userStories = userStoryRepository.findAllById(
                addSprintRequest.getUserStories().stream()
                        .map(UserStoryDTO::getId)
                        .collect(Collectors.toList())
        );

        Sprint sprint = new Sprint();
        sprint.setTitre(addSprintRequest.getTitre());
        sprint.setDescription(addSprintRequest.getDescription());
        sprint.setDate_debut(addSprintRequest.getDate_debut());
        sprint.setDate_fin(addSprintRequest.getDate_fin());
        sprint.setStatus(addSprintRequest.getStatus());
        sprint.setUserStories(userStories);

        Sprint savedSprint = sprintRepository.save(sprint);

        Project project = projectRepository.findAll().stream()
                .filter(proj -> proj.getNom() != null && proj.getNom().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s]", projectName)));

        project.getSprints().add(savedSprint);
        projectRepository.save(project);

        AddSprintResponse response = new AddSprintResponse();
      //  response.setId(savedSprint.getId());
        response.setTitre(savedSprint.getTitre());
        response.setDescription(savedSprint.getDescription());
        response.setDate_debut(savedSprint.getDate_debut());
        response.setDate_fin(savedSprint.getDate_fin());
        response.setStatus(savedSprint.getStatus());

        List<UserStoryDTO> userStoryDTOs = userStories.stream()
                .map(us -> {
                    UserStoryDTO dto = new UserStoryDTO();
                    dto.setId(us.getId());
                    dto.setCode(us.getCode());
                    dto.setTitre(us.getTitre());
                    dto.setDescription(us.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setUserStories(userStoryDTOs);

        response.setMessage(String.format("Sprint ajouté avec succès au projet [%s] : [Titre = %s, Description = %s, Status = %s, Date de debut = %s, Date de fin = %s]",
                projectName, response.getTitre(), response.getDescription(), response.getStatus(), response.getDate_debut(), response.getDate_fin()));

        return response;
    }

    @Override
    public UpdateSprintResponse updateSprint(String titre, UpdateSprintRequest updateSprintRequest) {
        Sprint sprintFound = sprintRepository.findAll().stream()
                .filter(bo -> bo.getTitre() != null && bo.getTitre().equals(titre))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun sprint existe avec le titre [%s] ", titre)));

        sprintFound.setDescription(updateSprintRequest.getDescription());
        sprintFound.setDate_debut(updateSprintRequest.getDate_debut());
        sprintFound.setDate_fin(updateSprintRequest.getDate_fin());
        sprintFound.setStatus(updateSprintRequest.getStatus());

        List<UserStory> userStories = sprintFound.getUserStories();
        if (updateSprintRequest.getUserStories() != null) {
            userStories = userStoryRepository.findAllById(
                    updateSprintRequest.getUserStories().stream()
                            .map(UserStoryDTO::getId)
                            .collect(Collectors.toList())
            );
        }
        sprintFound.setUserStories(userStories);

        Sprint savedSprint = sprintRepository.save(sprintFound);

        UpdateSprintResponse updateSprintResponse = new UpdateSprintResponse();
      //  updateSprintResponse.setId(savedSprint.getId());
        updateSprintResponse.setTitre(savedSprint.getTitre());
        updateSprintResponse.setDescription(savedSprint.getDescription());
        updateSprintResponse.setDate_debut(savedSprint.getDate_debut());
        updateSprintResponse.setDate_fin(savedSprint.getDate_fin());
        updateSprintResponse.setStatus(savedSprint.getStatus());

        List<UserStoryDTO> userStoryDTOs = userStories.stream()
                .map(us -> {
                    UserStoryDTO dto = new UserStoryDTO();
                    dto.setId(us.getId());
                    dto.setCode(us.getCode());
                    dto.setTitre(us.getTitre());
                    dto.setDescription(us.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        updateSprintResponse.setUserStories(userStoryDTOs);

        updateSprintResponse.setMessage(String.format("Sprint avec le titre [%s] a été modifié avec succès", titre));

        return updateSprintResponse;
    }


    @Override
    public String deleteSprintById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity sprint");
        Sprint sprintFound = sprintRepository.findAll().stream().filter(sprint -> sprint.getId()==id).findFirst().orElseThrow(
                () -> new BusinessException(String.format("No customer with identity %d exist in database", id))
        );
        sprintRepository.delete(sprintFound);
        return String.format("Sprint with identity %d is deleted with success", id);
    }
    @Override
    public List<SprintDTO> findByTitre(String titre) {
        return sprintRepository.findByTitreContainingIgnoreCase(titre.toLowerCase()).stream()
                .map(sprint -> modelMapper.map(sprint, SprintDTO.class)).collect(Collectors.toList());
    }
    @Override
    public SprintDTO searchByTitre(String titre) {
        return modelMapper.map(sprintRepository.findSprintByTitre(titre.toLowerCase()), SprintDTO.class);
    }
}
