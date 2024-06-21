package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Message.*;

import java.util.List;

public interface IMessageService {
    List<MessageDTO> getAllMessages();
    AddMessageResponse createMessage(AddMessageRequest addMessageRequest);
    UpdateMessageResponse updateMessage(Long id, UpdateMessageRequest updateMessageRequest);
    List<MessageDTO> getMessagesByChatRoomId(Long chatRoomId);
    List<MessageDTO> getMessagesByExpId(Long expId);
    String deleteMessage(Long id);
}
