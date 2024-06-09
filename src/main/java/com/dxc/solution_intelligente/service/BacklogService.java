package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.ProjectRepository;
import com.dxc.solution_intelligente.DTO.Backlog.*;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Project;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
        // Créer le backlog
        Backlog bo = modelMapper.map(addBacklogRequest, Backlog.class);
        String titre = bo.getTitre();
        backlogRepository.findBacklogByTitre(titre).ifPresent(backlog -> {
            throw new BusinessException(String.format("Backlog avec le titre [%s] déjà existe", titre));
        });

        // Sauvegarder le backlog d'abord
        Backlog savedBacklog = backlogRepository.save(bo);

        // Trouver le projet par nom
        Project project = projectRepository.findAll().stream()
                .filter(proj -> proj.getNom() != null && proj.getNom().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucun projet existe avec le nom [%s] ", projectName)));

        // Ajouter le backlog sauvegardé au projet
        project.getBacklogs().add(savedBacklog);
        Project savedProject = projectRepository.save(project);

        // Préparer la réponse
        AddBacklogResponse response = modelMapper.map(savedBacklog, AddBacklogResponse.class);
        response.setMessage(String.format("Backlog ajouté avec succès au projet [%s] : [Titre = %s, Description = %s, Status = %s]", projectName, response.getTitre(), response.getDescription(), response.getStatus()));

        return response;
    }


    @Override
    public UpdateBacklogResponse updateBacklog(String titre, UpdateBacklogRequest updateBacklogRequest) {
        Backlog backlogToPersist = modelMapper.map(updateBacklogRequest, Backlog.class);
        Backlog backlogFound = backlogRepository.findAll().stream().filter(bo-> bo.getTitre() != null && bo.getTitre().equals(titre)).findFirst().orElseThrow(
                ()-> new BusinessException(String.format("Aucun backlog existe avec le titre [%s] ", titre))
        );
        backlogToPersist.setId(backlogFound.getId());
        backlogFound.setTitre(titre);
        UpdateBacklogResponse updateBacklogResponse = modelMapper.map(backlogRepository.save(backlogToPersist), UpdateBacklogResponse.class);
        updateBacklogResponse.setMessage(String.format("Backlog avec le titre [%s] a ete modifie avec succes", titre));
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




}
