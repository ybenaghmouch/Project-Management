package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.TacheRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.Tache.*;
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
        Tache bo = modelMapper.map(addTacheRequest, Tache.class);
        String code = bo.getCode();
        System.out.println("password 1= "+ addTacheRequest.toString());
        //System.out.println("password 2= "+ bo.getPassword());
        tacheRepository.findBycode(code).ifPresent(a ->{
                    throw new BusinessException(String.format("Tache avec le meme code [%s] existe", code));

                }
        );
        AddTacheResponse response = modelMapper.map(tacheRepository.save(bo), AddTacheResponse.class);
        //response.setMessage(String.format("Tache : [code = %s, Prenom = %s, code = %s, Email = %s, Civility = %s, Specilite = %s]", response.getcode(), response.getCollaborateurs().toString(), response.getManager().toString(), response.getChefprojet().toString()));
        return response;
    }

    @Override
    public AddTacheResponse addTacheToUserStory(String userStoryCode, AddTacheRequest addTacheRequest) {
        User responsable1 = userRepository.findById(addTacheRequest.getResponsable().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        addTacheRequest.setResponsable(responsable1);
        // Mapper AddTacheRequest à Tache
        Tache tache = modelMapper.map(addTacheRequest, Tache.class);
        String code = tache.getCode();

        // Vérifier si une tâche avec le même code existe déjà
        tacheRepository.findBycode(code).ifPresent(existingTache -> {
            throw new BusinessException(String.format("Tache avec le code [%s] existe déjà", code));
        });

        // Sauvegarder la Tache d'abord
        Tache savedTache = tacheRepository.save(tache);

        // Trouver la User Story par code
        UserStory userStory = userStoryRepository.findBycode(userStoryCode)
                .orElseThrow(() -> new BusinessException(String.format("Aucune User Story existe avec le code [%s]", userStoryCode)));

        // Ajouter la Tache sauvegardée à la User Story
        userStory.getFeatures().add(savedTache);
        userStoryRepository.save(userStory);
        savedTache.setResponsable(userStory.getResponsable());
        // Préparer la réponse
        AddTacheResponse response = modelMapper.map(savedTache, AddTacheResponse.class);
        response.setMessage(String.format("Tache ajoutée avec succès à la User Story [%s] : [Code = %s, Titre = %s]", userStoryCode, response.getCode(), response.getTitre()));
       if(response.getResponsable()!=null ){
        User responsable = userRepository.findById(response.getResponsable().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        response.setResponsable(responsable);}

        return response;
    }

    @Override
    public String deleteTacheById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity tache");
        Tache tacheFound = tacheRepository.findAll().stream().filter(tache -> tache.getId()==id).findFirst().orElseThrow(
                () -> new BusinessException(String.format("No customer with identity %d exist in database", id))
        );
        tacheRepository.delete(tacheFound);
        return String.format("Tache with identity %d is deleted with success", id);
    }

    @Override
    public UpdateTacheResponse updateTache(String code, UpdateTacheRequest updateTacheRequest) {
        Tache tacheToPersist = modelMapper.map(updateTacheRequest, Tache.class);
        Tache tacheFound = tacheRepository.findAll().stream().filter(bo -> bo.getCode().equals(code)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("Tache avec le code [%s] deja existe!", code))
        );
        tacheToPersist.setId(tacheFound.getId());
        tacheToPersist.setCode(code);
        UpdateTacheResponse updateTacheResponse = modelMapper.map(tacheRepository.save(tacheToPersist), UpdateTacheResponse.class);
        updateTacheResponse.setMessage(String.format("Tache avec code [%s] a ete modifie avec succes !", code));
        return updateTacheResponse;
    }

    @Override
    public List<TacheDTO> findByCodeAndTitreContaining(String searchTerm) {
        return tacheRepository.findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(searchTerm.toLowerCase(),searchTerm.toLowerCase()).stream().
                map(tache -> modelMapper.map(tache, TacheDTO.class)).
                collect(Collectors.toList());
    }
}
