package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ChatRoomRepository;
import com.dxc.solution_intelligente.DAO.MessageRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Message.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IMessageService;
import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.Message;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class MessageService implements IMessageService {
    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddMessageResponse createMessage(AddMessageRequest addMessageRequest) {
        Message message = modelMapper.map(addMessageRequest, Message.class);

        User exp = userRepository.findById(addMessageRequest.getExp().getId())
                .orElseThrow(() -> new BusinessException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(addMessageRequest.getChatRoom().getId())
                .orElseThrow(() -> new BusinessException("ChatRoom not found"));

        message.setExp(exp);
        message.setChatRoom(chatRoom);

        message = messageRepository.save(message);

        AddMessageResponse response = modelMapper.map(message, AddMessageResponse.class);
        response.setMessage("Message created successfully");
        return response;
    }

    @Override
    public UpdateMessageResponse updateMessage(Long id, UpdateMessageRequest updateMessageRequest) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Message not found"));

        message.setContent(updateMessageRequest.getContent());
        message.setTime(updateMessageRequest.getTime());

        message = messageRepository.save(message);

        UpdateMessageResponse response = modelMapper.map(message, UpdateMessageResponse.class);
        response.setMessage("Message updated successfully");
        return response;
    }

    @Override
    public List<MessageDTO> getMessagesByChatRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId).stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getMessagesByExpId(Long expId) {
        return messageRepository.findByExpId(expId).stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Message not found"));

        messageRepository.delete(message);

        return "Message deleted successfully";
    }
}