package com.dxc.solution_intelligente.DTO.Backlog;

import com.dxc.solution_intelligente.DTO.UserStory.UserStoryDTO;
import com.dxc.solution_intelligente.service.model.UserStory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateBacklogRequest {
    private String titre;
    private String description;
    private String Status;
    private List<UserStoryDTO> userStories;
}
