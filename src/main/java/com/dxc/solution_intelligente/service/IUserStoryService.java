package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.UserStory.*;

import java.util.List;

public interface IUserStoryService {
    List<UserStoryDTO> getAllUserStorys();
    AddUserStoryResponse createUserStory(AddUserStoryRequest addUserStoryRequest);
    UpdateUserStoryResponse updateUserStory(String code, UpdateUserStoryRequest updateUserStoryRequest);
    List<UserStoryDTO> findByCodeAndTitreContaining(String searchTerm);
    AddUserStoryResponse addUserStoryToBacklog(String backlogtitle, AddUserStoryRequest addUserStoryRequest);
    UserStoryDTO findByCode(String searchTerm);
    String deleteUserStoryById(Long id);
}
