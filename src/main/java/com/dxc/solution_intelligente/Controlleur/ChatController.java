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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @MessageMapping("/chat.initiate")
    @SendTo("/topic/chatroom")
    @Transactional
    public ChatRoom initiateChat(@Payload List<Long> userIds) {
        if (userIds.size() != 2) {
            throw new IllegalArgumentException("Two user IDs are required to initiate a chat.");
        }

        User userA = userRepository.findById(userIds.get(0)).orElse(null);
        User userB = userRepository.findById(userIds.get(1)).orElse(null);

        if (userA == null || userB == null) {
            throw new IllegalArgumentException("One or both users not found.");
        }

        // Check for existing chat room
        Optional<ChatRoom> existingChatRoom = findChatRoomByParticipants(userA, userB);
        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();
        }

        // Create a new chat room
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setUsers(List.of(userA, userB));
        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload AddMessageRequest addMessageRequest) {
        User exp = userRepository.findById(addMessageRequest.getExp().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(addMessageRequest.getChatRoom().getId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        Message message = new Message();
        message.setExp(exp);
        message.setChatRoom(chatRoom);
        message.setContent(addMessageRequest.getContent());
        message.setTime(new Date());

        messageRepository.save(message);
        return message;
    }

    private Optional<ChatRoom> findChatRoomByParticipants(User userA, User userB) {
        return chatRoomRepository.findAll().stream()
                .filter(room -> room.getUsers().contains(userA) && room.getUsers().contains(userB))
                .findFirst();
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageDTO sendMessageWebSocket(@Payload AddMessageRequest addMessageRequest) {
        AddMessageResponse response = messageService.createMessage(addMessageRequest);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(response.getId());
        messageDTO.setContent(response.getContent());
        messageDTO.setTime(new Date());
        messageDTO.setExp(response.getExp());
        messageDTO.setChatRoom(response.getChatRoom());
        return messageDTO;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public String addUserWebSocket(@Payload AddChatRoomRequest addChatRoomRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = addChatRoomRequest.getUsers().get(0).getUsername();
        headerAccessor.getSessionAttributes().put("username", username);
        return username + " joined the chat!";
    }
}
