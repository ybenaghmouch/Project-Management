package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DAO.ChatRoomRepository;
import com.dxc.solution_intelligente.DAO.MessageRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.ChatRoom.AddChatRoomRequest;
import com.dxc.solution_intelligente.DTO.Message.AddMessageRequest;
import com.dxc.solution_intelligente.DTO.Message.AddMessageResponse;
import com.dxc.solution_intelligente.DTO.Message.MessageDTO;
import com.dxc.solution_intelligente.service.IMessageService;
import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.Message;
import com.dxc.solution_intelligente.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class ChatController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessageWebSocket(@Payload AddMessageRequest addMessageRequest) {
        AddMessageResponse response = messageService.createMessage(addMessageRequest);
        messagingTemplate.convertAndSend("/topic/chatroom/" + addMessageRequest.getChatRoom().getId().toString(), addMessageRequest);
       /* MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(response.getId());
        messageDTO.setContent(response.getContent());
        messageDTO.setTime(new Date());
        messageDTO.setExp(response.getExp());
        messageDTO.setChatRoom(response.getChatRoom());

        // Broadcast the message to the appropriate chat room topic
        String chatRoomId = messageDTO.getChatRoom().getId().toString();
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, messageDTO);*/
    }

    @MessageMapping("/chat.addUser")
    public void addUserWebSocket(@Payload AddChatRoomRequest addChatRoomRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = addChatRoomRequest.getUsers().get(0).getUsername();
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("username", username);
        headerAccessor.setSessionAttributes(sessionAttributes);
        messagingTemplate.convertAndSend("/topic/public", username + " joined the chat!");
    }

    private Optional<ChatRoom> findChatRoomByParticipants(User userA, User userB) {
        return chatRoomRepository.findAll().stream()
                .filter(room -> room.getUsers().contains(userA) && room.getUsers().contains(userB))
                .findFirst();
    }
}

