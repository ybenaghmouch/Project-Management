package com.dxc.solution_intelligente.service;


import com.dxc.solution_intelligente.DTO.chefProjet.*;

import java.util.List;

public interface IChefProjetService {
    List<ChefProjetDTO> getAllChefProjets();
    AddChefProjetResponse createChefProjet(AddChefProjetRequest addChefProjetRequest);
    UpdateChefProjetResponse updateChefProjet(String username, UpdateChefProjetRequest updateChefProjetRequest);
    List<ChefProjetDTO> findByUsernameContaining(String searchTerm);
}
