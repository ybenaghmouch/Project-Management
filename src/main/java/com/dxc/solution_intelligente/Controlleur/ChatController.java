package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.ChatRoom.AddChatRoomRequest;
import com.dxc.solution_intelligente.DTO.ChatRoom.ChatRoomDTO;
import com.dxc.solution_intelligente.DTO.Message.AddMessageRequest;
import com.dxc.solution_intelligente.DTO.Message.AddMessageResponse;
import com.dxc.solution_intelligente.DTO.Message.MessageDTO;
import com.dxc.solution_intelligente.service.ChatRoomService;
import com.dxc.solution_intelligente.service.IChatRoomService;
import com.dxc.solution_intelligente.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {

    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final IMessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageDTO sendMessageWebSocket(@Payload AddMessageRequest addMessageRequest) {
        AddMessageResponse response = messageService.createMessage(addMessageRequest);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(response.getId());
        messageDTO.setContent(response.getContent());
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