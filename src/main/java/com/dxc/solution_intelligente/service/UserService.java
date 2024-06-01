package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.RoleRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;

import com.dxc.solution_intelligente.DTO.User.AddUserRequest;
import com.dxc.solution_intelligente.DTO.User.AddUserResponse;
import com.dxc.solution_intelligente.DTO.User.UpdateUserRequest;
import com.dxc.solution_intelligente.DTO.User.UpdateUserResponse;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.*;
import lombok.AllArgsConstructor;
import org.apache.catalina.Manager;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service

@AllArgsConstructor

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;




    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(bo -> {
                    UserDTO dto = modelMapper.map(bo, UserDTO.class);
                    // Set the dtype field manually
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<UserDTO> findByUsernameContaining(String searchTerm) {
        return userRepository.findByUsernameContainingIgnoreCase(searchTerm.toLowerCase()).stream().
                map(bo -> modelMapper.map(bo, UserDTO.class)).
                collect(Collectors.toList());
    }


    @Override
    public List<UserDTO> findRoleAndByUsernameContaining(String role, String username) {
        return userRepository.findByRoleAndUsernameContainingIgnoreCase(role.toLowerCase(),username.toLowerCase()).stream().
                map(bo -> modelMapper.map(bo, UserDTO.class)).
                collect(Collectors.toList());
    }


    @Override
    public AddUserResponse createUser(AddUserRequest addUserRequest) {
        User bo=modelMapper.map(addUserRequest, User.class);
        String username = bo.getUsername();
        System.out.println("Password 1 = " + addUserRequest.toString());

        // Encode the password
        bo.setPassword(passwordEncoder.encode(bo.getPassword()));

        // Check if the username already exists
        userRepository.findByUsername(username).ifPresent(a -> {
            throw new BusinessException(String.format("User with the same username [%s] exists", username));
        });

        // Save the bo and prepare the response
        User savedUser = userRepository.save(bo);  // save as the specific bo type
        AddUserResponse response = modelMapper.map(savedUser, AddUserResponse.class);

        List<Role> authorities = response.getAuthorities();
        List<Role> completeAuthorities = authorities.stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        response.setAuthorities(completeAuthorities);

        response.setMessage(String.format("User: [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Speciality = %s, Role = %s]",
                response.getFirstName(), response.getLastName(), response.getUsername(),
                response.getEmail(), response.getCivility(), response.getSpeciality(), response.getAuthorities()));

        return response;
    }



    @Override
    public UpdateUserResponse updateUser(String username, UpdateUserRequest updateUserRequest) {
        User userFound = userRepository.findAll().stream()
                .filter(bo -> bo.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("User avec le username [%s] n'existe pas!", username)));

        // Use ModelMapper to map non-null properties from updateUserRequest to userFound
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(updateUserRequest, userFound);
        if (updateUserRequest.getPassword() != null) {
            userFound.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        // Authorities handling
        if (updateUserRequest.getAuthorities() != null && !updateUserRequest.getAuthorities().isEmpty()) {
            List<Role> authorities = updateUserRequest.getAuthorities().stream()
                    .map(role -> roleRepository.findById(role.getId())
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            userFound.setAuthorities(authorities);
        }

        User savedUser = userRepository.save(userFound);

        UpdateUserResponse updateUserResponse = modelMapper.map(savedUser, UpdateUserResponse.class);
        List<Role> authorities = updateUserResponse.getAuthorities();
        List<Role> completeAuthorities = authorities.stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        updateUserResponse.setAuthorities(completeAuthorities);
        updateUserResponse.setMessage(String.format("User avec username [%s] a été modifié avec succès !", username));

        return updateUserResponse;
    }
}
