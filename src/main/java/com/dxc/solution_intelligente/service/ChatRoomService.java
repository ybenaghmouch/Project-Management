package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ChatRoomRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.ChatRoom.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class ChatRoomService implements IChatRoomService {
    private final ModelMapper modelMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatRoomDTO> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddChatRoomResponse createChatRoom(AddChatRoomRequest addChatRoomRequest) {
        ChatRoom chatRoom = modelMapper.map(addChatRoomRequest, ChatRoom.class);

        // Fetch users by their IDs and set them to the chat room
        List<User> users = userRepository.findAllById(
                addChatRoomRequest.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );

        chatRoom.setUsers(users);

        chatRoom = chatRoomRepository.save(chatRoom);

        AddChatRoomResponse response = modelMapper.map(chatRoom, AddChatRoomResponse.class);
        response.setMessage("Chat room created successfully");
        return response;
    }

    @Override
    public UpdateChatRoomResponse updateChatRoom(Long id, UpdateChatRoomRequest updateChatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ChatRoom not found"));

        chatRoom.setNom(updateChatRoomRequest.getNom());

        // Fetch users by their IDs and set them to the chat room
        List<User> users = userRepository.findAllById(
                updateChatRoomRequest.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );

        chatRoom.setUsers(users);

        chatRoom = chatRoomRepository.save(chatRoom);

        UpdateChatRoomResponse response = modelMapper.map(chatRoom, UpdateChatRoomResponse.class);
        response.setMessage("Chat room updated successfully");
        return response;
    }

    @Override
    public List<ChatRoomDTO> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUsersId(userId);
        return chatRooms.stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomDTO findChatRoomByUsers(Long userAId, Long userBId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        Optional<ChatRoom> chatRoom = chatRooms.stream()
                .filter(cr -> cr.getUsers().stream().map(User::getId).collect(Collectors.toList()).containsAll(List.of(userAId, userBId)))
                .findFirst();
        return chatRoom.map(cr -> modelMapper.map(cr, ChatRoomDTO.class)).orElse(null);
    }

}