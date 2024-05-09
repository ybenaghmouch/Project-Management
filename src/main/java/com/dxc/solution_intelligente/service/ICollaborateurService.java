package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.collaborateur.*;

import java.util.List;

public interface ICollaborateurService {
    List<CollaborateurDTO> getAllCollaborateurs();
    AddCollaborateurResponse createCollaborateur(AddCollaborateurRequest addCollaborateurRequest);
    UpdateCollaborateurResponse updateCollaborateur(String username, UpdateCollaborateurRequest updateCollaborateurRequest);

}
