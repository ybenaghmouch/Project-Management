package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.User.*;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    AddUserResponse createUser(AddUserRequest addUserRequest);
    UpdateUserResponse updateUser(String username, UpdateUserRequest updateUserRequest);
    List<UserDTO> findByUsernameContaining(String searchTerm);
}
