package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.AdminRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Admin.AddAdminResponse;
import com.dxc.solution_intelligente.DTO.User.AddUserRequest;
import com.dxc.solution_intelligente.DTO.User.AddUserResponse;
import com.dxc.solution_intelligente.DTO.User.UpdateUserRequest;
import com.dxc.solution_intelligente.DTO.User.UpdateUserResponse;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.*;
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

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;




    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().
                map(bo -> modelMapper.map(bo, UserDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findByUsernameContaining(String searchTerm) {
        return userRepository.findByUsernameContainingIgnoreCase(searchTerm.toLowerCase()).stream().
                map(bo -> modelMapper.map(bo, UserDTO.class)).
                collect(Collectors.toList());
    }


    @Override
    public AddUserResponse createUser(AddUserRequest addUserRequest) {
        User bo;
        String role = addUserRequest.getRole().toLowerCase();

        // Map the addUserRequest to the appropriate class based on the role
        switch (role) {
            case "admin":
                bo = modelMapper.map(addUserRequest, Admin.class);
                break;
            case "chefprojet":
                bo = modelMapper.map(addUserRequest, ChefProjet.class);
                break;
            case "manager":
                bo = modelMapper.map(addUserRequest, Manager.class);
                break;
            case "collaborateur":
                bo = modelMapper.map(addUserRequest, Collaborateur.class);
                break;
            default:
                throw new BusinessException("Unsupported role type: " + addUserRequest.getRole());
        }

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
        response.setMessage(String.format("User: [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Speciality = %s, Role = %s]",
                response.getFirstName(), response.getLastName(), response.getUsername(),
                response.getEmail(), response.getCivility(), response.getSpeciality(), savedUser.getClass().getSimpleName()));

        return response;
    }



    @Override
    public UpdateUserResponse updateUser(String username, UpdateUserRequest updateUserRequest) {
        User userToPersist = modelMapper.map(updateUserRequest, User.class);
        User userFound = userRepository.findAll().stream().filter(bo -> bo.getUsername().equals(username)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("User avec le username [%s] deja existe!", username))
        );
        userToPersist.setId(userFound.getId());
        userToPersist.setUsername(username);
        UpdateUserResponse updateUserResponse = modelMapper.map(userRepository.save(userToPersist), UpdateUserResponse.class);
        updateUserResponse.setMessage(String.format("User avec username [%s] a ete modifie avec succes !", username));
        return updateUserResponse;
    }
}
