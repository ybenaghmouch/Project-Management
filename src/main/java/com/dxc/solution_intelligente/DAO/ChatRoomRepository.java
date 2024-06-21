package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    ChatRoom findByNom(String nom);


    List<ChatRoom> findByUsersId(Long userId);



}
