package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Conge.CongeDTO;
import com.dxc.solution_intelligente.DTO.Conge.*;

import java.util.List;

public interface ICongeService {
    List<CongeDTO> getAllConges();
    AddCongeResponse createConge(AddCongeRequest addCongeRequest);
    UpdateCongeResponse updateConge(String name, UpdateCongeRequest updateCongeRequest);
    //List<CongeDTO> findCongeByManager(String username);
   //List<CongeDTO> findByName(String searchTerm);
    //CongeDTO findByexactName(String searchTerm);
}
