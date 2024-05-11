package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.collaborateur.*;
import com.dxc.solution_intelligente.service.model.Collaborateur;

import java.util.List;
import java.util.Optional;

public interface ICollaborateurService {
    List<CollaborateurDTO> getAllCollaborateurs();
    AddCollaborateurResponse createCollaborateur(AddCollaborateurRequest addCollaborateurRequest);
    UpdateCollaborateurResponse updateCollaborateur(String username, UpdateCollaborateurRequest updateCollaborateurRequest);
    List<CollaborateurDTO> findByUsernameContaining(String searchTerm);
}
