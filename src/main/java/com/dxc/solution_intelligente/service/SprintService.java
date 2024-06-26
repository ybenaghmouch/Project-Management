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
        Sprint bo = modelMapper.map(addSprintRequest, Sprint.class);
        String titre = bo.getTitre();
        sprintRepository.findSprintByTitre(titre).ifPresent(sprint -> {
            throw new BusinessException(String.format("Sprint avec le titre [%s] deja exite", titre));
        });
        AddSprintResponse response = modelMapper.map(sprintRepository.save(bo), AddSprintResponse.class);
        /*User manager = userRepository.findById(response.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        response.setManager(manager);*/
        response.setMessage(String.format("Sprint : [Titre = %s, Description = %s, Status = %s, Date de debut = %s, Date de fin = %s]", response.getTitre(), response.getDescription(), response.getStatus(), response.getDate_debut(), response.getDate_fin()));
        return response;
    }


    

    @Override
    public UpdateSprintResponse updateSprint(String titre, UpdateSprintRequest updateSprintRequest) {
        Sprint sprintToPersist = modelMapper.map(updateSprintRequest, Sprint.class);
        Sprint sprintFound = sprintRepository.findAll().stream().filter(bo-> bo.getTitre() != null && bo.getTitre().equals(titre)).findFirst().orElseThrow(
                ()-> new BusinessException(String.format("Aucun sprint existe avec le titre [%s] ", titre))
        );
        sprintToPersist.setId(sprintFound.getId());
        sprintFound.setTitre(titre);
        UpdateSprintResponse updateSprintResponse = modelMapper.map(sprintRepository.save(sprintToPersist), UpdateSprintResponse.class);
        updateSprintResponse.setMessage(String.format("Sprint avec le titre [%s] a ete modifie avec succes", titre));
        return updateSprintResponse;
    }

    @Override
    public List<SprintDTO> findByTitre(String titre) {
        return sprintRepository.findByTitreContainingIgnoreCase(titre.toLowerCase()).stream()
                .map(sprint -> modelMapper.map(sprint, SprintDTO.class)).collect(Collectors.toList());
    }


    public AddSprintResponse addSprintToProject(String projectName, AddSprintRequest addSprintRequest) {
        // Map the AddSprintRequest to a Sprint entity
        Sprint bo = modelMapper.map(addSprintRequest, Sprint.class);
        String titre = bo.getTitre();

        // Check if a sprint with the same title already exists
        sprintRepository.findSprintByTitre(titre).ifPresent(sprint -> {
            throw new BusinessException(String.format("Sprint avec le titre [%s] déjà existe", titre));
        });

        // Retrieve UserStory entities by their codes
        List<String> userStoryCodes = addSprintRequest.getUserStories().stream()
                .map(UserStoryDTO::getCode)
                .collect(Collectors.toList());
        System.out.println("tester"+userStoryCodes);
        List<UserStory> userStories = userStoryRepository.findAll().stream()
                .filter(us -> userStoryCodes.contains(us.getCode()))
                .collect(Collectors.toList());

        // Assign the retrieved UserStory entities to the Sprint
        bo.setUserStories(userStories);

        // Save the Sprint entity
        Sprint savedSprint = sprintRepository.save(bo);

        // Find the Project entity by its name
        Project project = projectRepository.findAll().stream()
                .filter(proj -> proj.getNom() != null && proj.getNom().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s]", projectName)));

        // Add the saved Sprint to the Project
        project.getSprints().add(savedSprint);

        // Save the updated Project entity
        Project savedProject = projectRepository.save(project);

        // Map the saved Sprint entity to the AddSprintResponse
        AddSprintResponse response = modelMapper.map(savedSprint, AddSprintResponse.class);
        response.setMessage(String.format("Sprint ajouté avec succes au projet [%s] : Titre = %s", projectName, response.getTitre()));

        return response;
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
    public SprintDTO searchByTitre(String titre) {
        return modelMapper.map(sprintRepository.findSprintByTitre(titre.toLowerCase()), SprintDTO.class);
    }
}
