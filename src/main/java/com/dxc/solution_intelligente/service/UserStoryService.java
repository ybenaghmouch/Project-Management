package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.BacklogRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
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
        userStoryRepository.findBycode(addUserStoryRequest.getCode()).ifPresent(a -> {
            throw new BusinessException(String.format("UserStory avec le code [%s] existe déjà", addUserStoryRequest.getCode()));
        });

        User responsable = null;
        if (addUserStoryRequest.getResponsable() != null) {
            responsable = userRepository.findById(addUserStoryRequest.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Responsable not found"));
        }

        UserStory userStory = new UserStory();
        userStory.setCode(addUserStoryRequest.getCode());
        userStory.setTitre(addUserStoryRequest.getTitre());
        userStory.setDescription(addUserStoryRequest.getDescription());
        userStory.setPriority(addUserStoryRequest.getPriority());
        userStory.setStatut(addUserStoryRequest.getStatut());
        userStory.setResponsable(responsable);
        userStory.setFeatures(null);

        UserStory savedUserStory = userStoryRepository.save(userStory);

        AddUserStoryResponse response = new AddUserStoryResponse();
        response.setId(savedUserStory.getId());
        response.setCode(savedUserStory.getCode());
        response.setTitre(savedUserStory.getTitre());
        response.setDescription(savedUserStory.getDescription());
        response.setPriority(savedUserStory.getPriority());
        response.setStatut(savedUserStory.getStatut());

        if (responsable != null) {
            UserDTO responsableDTO = modelMapper.map(responsable, UserDTO.class);
            response.setResponsable(responsableDTO);
        } else {
            response.setResponsable(null);
        }

        response.setMessage(String.format("UserStory : [Code = %s, Titre = %s, Description = %s, Priority = %d, Statut = %s]",
                response.getCode(), response.getTitre(), response.getDescription(), response.getPriority(), response.getStatut()));

        return response;
    }

    @Override
    public AddUserStoryResponse addUserStoryToBacklog(String backlogTitre, AddUserStoryRequest addUserStoryRequest) {
        userStoryRepository.findBycode(addUserStoryRequest.getCode()).ifPresent(a -> {
            throw new BusinessException(String.format("UserStory avec le code [%s] existe déjà", addUserStoryRequest.getCode()));
        });

        User responsable = null;
        if (addUserStoryRequest.getResponsable() != null) {
            responsable = userRepository.findById(addUserStoryRequest.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Responsable not found"));
        }

        UserStory userStory = new UserStory();
        userStory.setCode(addUserStoryRequest.getCode());
        userStory.setTitre(addUserStoryRequest.getTitre());
        userStory.setDescription(addUserStoryRequest.getDescription());
        userStory.setPriority(addUserStoryRequest.getPriority());
        userStory.setStatut(addUserStoryRequest.getStatut());
        userStory.setResponsable(responsable);
        userStory.setFeatures(null);

        UserStory savedUserStory = userStoryRepository.save(userStory);

        Backlog backlog = backlogRepository.findBacklogByTitre(backlogTitre)
                .orElseThrow(() -> new BusinessException(String.format("Aucun backlog existe avec le titre [%s]", backlogTitre)));

        backlog.getUserStories().add(savedUserStory);
        backlogRepository.save(backlog);

        AddUserStoryResponse response = new AddUserStoryResponse();
        response.setId(savedUserStory.getId());
        response.setCode(savedUserStory.getCode());
        response.setTitre(savedUserStory.getTitre());
        response.setDescription(savedUserStory.getDescription());
        response.setPriority(savedUserStory.getPriority());
        response.setStatut(savedUserStory.getStatut());

        if (responsable != null) {
            UserDTO responsableDTO = modelMapper.map(responsable, UserDTO.class);
            response.setResponsable(responsableDTO);
        } else {
            response.setResponsable(null);
        }

        response.setMessage(String.format("UserStory ajoutée avec succès au backlog [%s] : [Code = %s, Description = %s]",
                backlogTitre, response.getCode(), response.getDescription()));

        return response;
    }

    @Override
    public UpdateUserStoryResponse updateUserStory(String code, UpdateUserStoryRequest updateUserStoryRequest) {
        UserStory userStoryFound = userStoryRepository.findAll().stream()
                .filter(bo -> bo.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("UserStory avec le code [%s] n'existe pas", code)));

        User responsable = null;
        if (updateUserStoryRequest.getResponsable() != null) {
            responsable = userRepository.findById(updateUserStoryRequest.getResponsable().getId())
                    .orElseThrow(() -> new BusinessException("Responsable not found"));
        }

        userStoryFound.setTitre(updateUserStoryRequest.getTitre());
        userStoryFound.setDescription(updateUserStoryRequest.getDescription());
        userStoryFound.setPriority(updateUserStoryRequest.getPriority());
        userStoryFound.setStatut(updateUserStoryRequest.getStatut());
        userStoryFound.setResponsable(responsable);

        UserStory savedUserStory = userStoryRepository.save(userStoryFound);

        UpdateUserStoryResponse updateUserStoryResponse = new UpdateUserStoryResponse();
        updateUserStoryResponse.setId(savedUserStory.getId());
        updateUserStoryResponse.setCode(savedUserStory.getCode());
        updateUserStoryResponse.setTitre(savedUserStory.getTitre());
        updateUserStoryResponse.setDescription(savedUserStory.getDescription());
        updateUserStoryResponse.setPriority(savedUserStory.getPriority());
        updateUserStoryResponse.setStatut(savedUserStory.getStatut());

        if (responsable != null) {
            UserDTO responsableDTO = modelMapper.map(responsable, UserDTO.class);
            updateUserStoryResponse.setResponsable(responsableDTO);
        } else {
            updateUserStoryResponse.setResponsable(null);
        }

        updateUserStoryResponse.setMessage(String.format("UserStory avec code [%s] a été modifiée avec succès", code));

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
