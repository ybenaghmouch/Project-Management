package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Equipe.*;

import java.util.List;

public interface IEquipeService {
    List<EquipeDTO> getAllEquipes();
    AddEquipeResponse createEquipe(AddEquipeRequest addEquipeRequest);
    UpdateEquipeResponse updateEquipe(String username, UpdateEquipeRequest updateEquipeRequest);
    List<EquipeDTO> findByNomContaining(String searchTerm);
    EquipeDTO findByNom(String searchTerm);
    String deleteEquipeById(Long id);
}
