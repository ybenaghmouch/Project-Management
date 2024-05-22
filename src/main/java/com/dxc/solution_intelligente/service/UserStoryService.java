package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.UserStoryRepository;
import com.dxc.solution_intelligente.DTO.UserStory.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
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
    private final ModelMapper modelMapper;
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
}
