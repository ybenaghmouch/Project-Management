package com.dxc.solution_intelligente.DTO.Message;

import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AddMessageRequest {

    private User exp;
    private Date time;
    private String content;
    private ChatRoom chatRoom;
}
