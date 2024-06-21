package com.dxc.solution_intelligente.DTO.Message;

import com.dxc.solution_intelligente.service.model.ChatRoom;
import com.dxc.solution_intelligente.service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AddMessageResponse {
    private Long id;
    private User exp;
    private Date time;
    private ChatRoom chatRoom;
    private String content;
    private String message;

}
