package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Sprint.*;

import java.util.List;

public interface ISprintService {
    List<SprintDTO> getAllSprints();
    AddSprintResponse createSprint(AddSprintRequest addSprintRequest);
    UpdateSprintResponse updateSprint(String titre, UpdateSprintRequest updateSprintRequest);
    List<SprintDTO> findByTitre(String titre);
    AddSprintResponse addSprintToProject(String projectName, AddSprintRequest addSprintRequest);
    String deleteSprintById(Long id);
    SprintDTO searchByTitre(String titre);
}
