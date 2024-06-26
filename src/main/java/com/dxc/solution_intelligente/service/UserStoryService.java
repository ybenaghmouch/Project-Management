package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.UserStory.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Backlog;
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
@Transactional
@AllArgsConstructor
public class UserStoryService implements IUserStoryService {
    private final UserStoryRepository userStoryRepository;
    private final BacklogRepository backlogRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;




    @Override
    public List<UserStoryDTO> getAllUserStorys() {
        return userStoryRepository.findAll().stream().
                map(userStory -> modelMapper.map(userStory, UserStoryDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddUserStoryResponse createUserStory(AddUserStoryRequest addUserStoryRequest) {
        UserStory bo = modelMapper.map(addUserStoryRequest, UserStory.class);
        String code = bo.getCode();
        System.out.println("password 1= "+ addUserStoryRequest.toString());
        //System.out.println("password 2= "+ bo.getPassword());
        userStoryRepository.findBycode(code).ifPresent(a ->{
                    throw new BusinessException(String.format("UserStory avec le meme code [%s] existe", code));

                }
        );
        AddUserStoryResponse response = modelMapper.map(userStoryRepository.save(bo), AddUserStoryResponse.class);
        //response.setMessage(String.format("UserStory : [code = %s, Prenom = %s, code = %s, Email = %s, Civility = %s, Specilite = %s]", response.getcode(), response.getCollaborateurs().toString(), response.getManager().toString(), response.getChefprojet().toString()));
        return response;
    }
    @Override
    public AddUserStoryResponse addUserStoryToBacklog(String backlogtitle, AddUserStoryRequest addUserStoryRequest) {
        // Créer la UserStory
        UserStory bo = modelMapper.map(addUserStoryRequest, UserStory.class);
        String code = bo.getCode();
        userStoryRepository.findBycode(code).ifPresent(userStory -> {
            throw new BusinessException(String.format("UserStory avec le code [%s] existe déjà", code));
        });

        // Sauvegarder la UserStory d'abord
        UserStory savedUserStory = userStoryRepository.save(bo);

        // Trouver le backlog par ID
        Backlog backlog = backlogRepository.findBacklogByTitre(backlogtitle)
                .orElseThrow(() -> new BusinessException(String.format("Aucun backlog existe avec l'ID [%s] ", backlogtitle)));

        // Ajouter la UserStory sauvegardée au backlog
        backlog.getUserStories().add(savedUserStory);
        backlogRepository.save(backlog);

        // Préparer la réponse
        AddUserStoryResponse response = modelMapper.map(savedUserStory, AddUserStoryResponse.class);
        response.setMessage(String.format("UserStory ajoutée avec succès au backlog [%s] : [Code = %s, Description = %s]", backlogtitle, response.getCode(), response.getDescription()));
        if(response.getResponsable()!=null ){
            User responsable = userRepository.findById(response.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Manager not found"));
            response.setResponsable(responsable);}
        return response;
    }

    @Override
    public UpdateUserStoryResponse updateUserStory(String code, UpdateUserStoryRequest updateUserStoryRequest) {
        UserStory userStoryToPersist = modelMapper.map(updateUserStoryRequest, UserStory.class);
        UserStory userStoryFound = userStoryRepository.findAll().stream().filter(bo -> bo.getCode().equals(code)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("UserStory avec le code [%s] deja existe!", code))
        );
        userStoryToPersist.setId(userStoryFound.getId());
        userStoryToPersist.setCode(code);
        UpdateUserStoryResponse updateUserStoryResponse = modelMapper.map(userStoryRepository.save(userStoryToPersist), UpdateUserStoryResponse.class);
        updateUserStoryResponse.setMessage(String.format("UserStory avec code [%s] a ete modifie avec succes !", code));
        return updateUserStoryResponse;
    }

    @Override
    public List<UserStoryDTO> findByCodeAndTitreContaining(String searchTerm) {
        return userStoryRepository.findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(searchTerm.toLowerCase(),searchTerm.toLowerCase()).stream().
                map(userStory -> modelMapper.map(userStory, UserStoryDTO.class)).
                collect(Collectors.toList());
    }
    public UserStoryDTO findByCode(String searchTerm) {
        return modelMapper.map(userStoryRepository.findBycode(searchTerm.toLowerCase()), UserStoryDTO.class);
    }

    @Override
    public String deleteUserStoryById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity User Story");
        UserStory userStoryFound = userStoryRepository.findAll().stream().filter(tache -> tache.getId()==id).findFirst().orElseThrow(
                () -> new BusinessException(String.format("No User Story with identity %d exist in database", id))
        );
        userStoryRepository.delete(userStoryFound);
        return String.format("User Story with identity %d is deleted with success", id);
    }
}
