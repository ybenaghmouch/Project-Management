package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatRoomId(Long chatRoomId);
    List<Message> findByExpId(Long expId);


}