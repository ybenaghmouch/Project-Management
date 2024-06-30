package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DAO.ChatRoomRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.ChatRoom.AddChatRoomRequest;
import com.dxc.solution_intelligente.DTO.ChatRoom.AddChatRoomResponse;
import com.dxc.solution_intelligente.DTO.ChatRoom.ChatRoomDTO;
import com.dxc.solution_intelligente.DTO.Message.AddMessageRequest;
import com.dxc.solution_intelligente.DTO.Message.AddMessageResponse;
import com.dxc.solution_intelligente.DTO.Message.MessageDTO;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.ChatRoomService;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IMessageService;
import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/chat")
public class RestChatController {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final IMessageService messageService;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/initiate")
    public ResponseEntity<?> initiateChat(@RequestParam("userA") Long userAId, @RequestParam("userB") Long userBId) {
        try {
            // Check if a chat room already exists between User A and User B
            ChatRoomDTO chatRoom = chatRoomService.findChatRoomByUsers(userAId, userBId);

            if (chatRoom != null) {
                // Existing chat room found, return its details
                return ResponseEntity.ok(chatRoom.getId());
            } else {
                // No existing chat room found, create a new one
                User userA = userRepository.findById(userAId)
                        .orElseThrow(() -> new BusinessException("User A not found"));
                User userB = userRepository.findById(userBId)
                        .orElseThrow(() -> new BusinessException("User B not found"));

                AddChatRoomRequest addChatRoomRequest = new AddChatRoomRequest();
                addChatRoomRequest.setNom("Chat between User " + userAId + " and User " + userBId);
                addChatRoomRequest.setUsers(List.of(modelMapper.map(userA, UserDTO.class), modelMapper.map(userB, UserDTO.class)));

                AddChatRoomResponse addChatRoomResponse = chatRoomService.createChatRoom(addChatRoomRequest);

                // Convert AddChatRoomResponse to ChatRoomDTO for consistency
                ChatRoomDTO newChatRoom = new ChatRoomDTO();
                newChatRoom.setId(addChatRoomResponse.getId());
                newChatRoom.setNom(addChatRoomResponse.getNom());
                newChatRoom.setUsers(addChatRoomResponse.getUsers());

                // Return the details of the new chat room
                return ResponseEntity.ok(newChatRoom.getId());
            }
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<AddMessageResponse> sendMessage(@RequestBody AddMessageRequest addMessageRequest, @RequestParam("exp") Long expId) {
        try {
            addMessageRequest.getExp().setId(expId);
            AddMessageResponse response = messageService.createMessage(addMessageRequest);
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            AddMessageResponse response = messageService.createMessage(addMessageRequest);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestParam("chatRoomId") Long chatRoomId, @RequestParam("exp") Long expId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByChatRoomId(chatRoomId);
            return ResponseEntity.ok(messages);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/rooms/user/{userId}")
    public ResponseEntity<?> getChatRoomsByUserId(@PathVariable Long userId) {
        try {
            List<ChatRoomDTO> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
            return ResponseEntity.ok(chatRooms);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    //TODO:switch to DTO
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable Long roomId) {
        try {
            ChatRoomDTO chatRoom = chatRoomService.findChatRoomById(roomId);
            return ResponseEntity.ok(chatRoom);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



}
