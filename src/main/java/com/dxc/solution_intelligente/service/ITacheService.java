package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Tache.*;

import java.util.List;

public interface ITacheService {
    List<TacheDTO> getAllTaches();
    AddTacheResponse createTache(AddTacheRequest addTacheRequest);
    UpdateTacheResponse updateTache(String code, UpdateTacheRequest updateTacheRequest);
    List<TacheDTO> findByCodeAndTitreContaining(String searchTerm);
}
