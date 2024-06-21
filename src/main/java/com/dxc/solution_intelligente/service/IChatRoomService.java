package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.ChatRoom.*;

import java.util.List;

public interface IChatRoomService {
    List<ChatRoomDTO> getAllChatRooms();
    AddChatRoomResponse createChatRoom(AddChatRoomRequest addChatRoomRequest);
    UpdateChatRoomResponse updateChatRoom(Long id, UpdateChatRoomRequest updateChatRoomRequest);
    List<ChatRoomDTO> getChatRoomsByUserId(Long userId);
    ChatRoomDTO findChatRoomByUsers(Long userAId, Long userBId);
}
