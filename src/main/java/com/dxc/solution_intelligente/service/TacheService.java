package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.TacheRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.Tache.*;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Sprint;
import com.dxc.solution_intelligente.service.model.Tache;
import com.dxc.solution_intelligente.service.model.User;
import com.dxc.solution_intelligente.service.model.UserStory;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service

@AllArgsConstructor

public class TacheService implements ITacheService{

    private final TacheRepository tacheRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;


    @Override
    public List<TacheDTO> getAllTaches() {
        return tacheRepository.findAll().stream().
                map(tache -> modelMapper.map(tache, TacheDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddTacheResponse createTache(AddTacheRequest addTacheRequest) {
        String code = addTacheRequest.getCode();
        tacheRepository.findBycode(code).ifPresent(a -> {
            throw new BusinessException(String.format("Tache avec le code [%s] existe déjà", code));
        });

        Tache tache = new Tache();
        tache.setCode(addTacheRequest.getCode());
        tache.setTitre(addTacheRequest.getTitre());
        tache.setDescription(addTacheRequest.getDescription());
        tache.setPriority(addTacheRequest.getPriority());
        tache.setStatut(addTacheRequest.getStatut());

        Tache savedTache = tacheRepository.save(tache);

        AddTacheResponse response = new AddTacheResponse();
        response.setId(savedTache.getId());
        response.setCode(savedTache.getCode());
        response.setTitre(savedTache.getTitre());
        response.setDescription(savedTache.getDescription());
        response.setPriority(savedTache.getPriority());
        response.setStatut(savedTache.getStatut());
        response.setResponsable(null);

        response.setMessage(String.format("Tache : [Code = %s, Titre = %s, Description = %s, Priority = %d, Statut = %s]",
                response.getCode(), response.getTitre(), response.getDescription(), response.getPriority(), response.getStatut()));

        return response;
    }

    @Override
    public AddTacheResponse addTacheToUserStory(String userStoryCode, AddTacheRequest addTacheRequest) {
        User responsable = null;
        if (addTacheRequest.getResponsable() != null) {
            responsable = userRepository.findById(addTacheRequest.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Responsable not found"));
        }

        Tache tache = new Tache();
        tache.setCode(addTacheRequest.getCode());
        tache.setTitre(addTacheRequest.getTitre());
        tache.setDescription(addTacheRequest.getDescription());
        tache.setPriority(addTacheRequest.getPriority());
        tache.setStatut(addTacheRequest.getStatut());
        tache.setResponsable(responsable);

        tacheRepository.findBycode(tache.getCode()).ifPresent(existingTache -> {
            throw new BusinessException(String.format("Tache avec le code [%s] existe déjà", tache.getCode()));
        });

        Tache savedTache = tacheRepository.save(tache);

        UserStory userStory = userStoryRepository.findBycode(userStoryCode)
                .orElseThrow(() -> new BusinessException(String.format("Aucune User Story existe avec le code [%s]", userStoryCode)));

        userStory.getFeatures().add(savedTache);
        userStoryRepository.save(userStory);

        AddTacheResponse response = new AddTacheResponse();
        response.setId(savedTache.getId());
        response.setCode(savedTache.getCode());
        response.setTitre(savedTache.getTitre());
        response.setDescription(savedTache.getDescription());
        response.setPriority(savedTache.getPriority());
        response.setStatut(savedTache.getStatut());

        if (responsable != null) {
            UserDTO responsableDTO = new UserDTO();
            responsableDTO.setId(responsable.getId());
            responsableDTO.setUsername(responsable.getUsername());
            response.setResponsable(responsableDTO);
        } else {
            response.setResponsable(null);
        }

        response.setMessage(String.format("Tache ajoutée avec succès à la User Story [%s] : [Code = %s, Titre = %s]", userStoryCode, response.getCode(), response.getTitre()));

        return response;
    }

    @Override
    public String deleteTacheById(Long id) {
        if (id == null) {
            throw new BusinessException("Enter a correct identity tache");
        }
        Tache tacheFound = tacheRepository.findAll().stream()
                .filter(tache -> tache.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("No tache with identity %d exists in database", id)));

        tacheRepository.delete(tacheFound);
        return String.format("Tache with identity %d is deleted with success", id);
    }

    @Override
    public UpdateTacheResponse updateTache(String code, UpdateTacheRequest updateTacheRequest) {
        Tache tacheFound = tacheRepository.findAll().stream()
                .filter(bo -> bo.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Tache avec le code [%s] n'existe pas", code)));

        User responsable = null;
        if (updateTacheRequest.getResponsable() != null) {
            responsable = userRepository.findById(updateTacheRequest.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Responsable not found"));
        }

        tacheFound.setTitre(updateTacheRequest.getTitre());
        tacheFound.setDescription(updateTacheRequest.getDescription());
        tacheFound.setPriority(updateTacheRequest.getPriority());
        tacheFound.setStatut(updateTacheRequest.getStatut());
        tacheFound.setResponsable(responsable);

        Tache savedTache = tacheRepository.save(tacheFound);

        UpdateTacheResponse updateTacheResponse = new UpdateTacheResponse();
        updateTacheResponse.setId(savedTache.getId());
        updateTacheResponse.setCode(savedTache.getCode());
        updateTacheResponse.setTitre(savedTache.getTitre());
        updateTacheResponse.setDescription(savedTache.getDescription());
        updateTacheResponse.setPriority(savedTache.getPriority());
        updateTacheResponse.setStatut(savedTache.getStatut());

        if (responsable != null) {
            UserDTO responsableDTO = new UserDTO();
            responsableDTO.setId(responsable.getId());
            responsableDTO.setUsername(responsable.getUsername());
            updateTacheResponse.setResponsable(responsableDTO);
        } else {
            updateTacheResponse.setResponsable(null);
        }

        updateTacheResponse.setMessage(String.format("Tache avec code [%s] a été modifiée avec succès", code));

        return updateTacheResponse;
    }


    @Override
    public List<TacheDTO> findByCodeAndTitreContaining(String searchTerm) {
        return tacheRepository.findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(searchTerm.toLowerCase(),searchTerm.toLowerCase()).stream().
                map(tache -> modelMapper.map(tache, TacheDTO.class)).
                collect(Collectors.toList());
    }
}
