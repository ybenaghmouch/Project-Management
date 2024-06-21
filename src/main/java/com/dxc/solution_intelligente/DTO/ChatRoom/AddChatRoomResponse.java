package com.dxc.solution_intelligente.DTO.ChatRoom;

import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AddChatRoomResponse {
    private Long id;
    private String nom;
    private List<User> users;
    private String message;
}
