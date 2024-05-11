package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.CollaborateurRepository;
import com.dxc.solution_intelligente.DTO.collaborateur.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CollaborateurService implements ICollaborateurService{

    private final CollaborateurRepository collaborateurRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;




    @Override
    public List<CollaborateurDTO> getAllCollaborateurs() {
        return collaborateurRepository.findAll().stream().
                map(collaborateur -> modelMapper.map(collaborateur, CollaborateurDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddCollaborateurResponse createCollaborateur(AddCollaborateurRequest addCollaborateurRequest) {
        Collaborateur bo = modelMapper.map(addCollaborateurRequest, Collaborateur.class);
        String username = bo.getUsername();
        System.out.println("password 1= "+ addCollaborateurRequest.toString());
        bo.setPassword(passwordEncoder.encode(bo.getPassword()));
        //System.out.println("password 2= "+ bo.getPassword());
        collaborateurRepository.findByUsername(username).ifPresent(a ->{
            throw new BusinessException(String.format("Collaborateur avec le meme username [%s] existe", username));

                }
        );
        AddCollaborateurResponse response = modelMapper.map(collaborateurRepository.save(bo), AddCollaborateurResponse.class);
        response.setMessage(String.format("Collaborateur : [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Specilite = %s]", response.getFirstName(), response.getLastName(), response.getUsername(), response.getEmail(), response.getCivility(), response.getSpeciality()));
        return response;
    }

    @Override
    public UpdateCollaborateurResponse updateCollaborateur(String username, UpdateCollaborateurRequest updateCollaborateurRequest) {
        Collaborateur collaborateurToPersist = modelMapper.map(updateCollaborateurRequest, Collaborateur.class);
        Collaborateur collaborateurFound = collaborateurRepository.findAll().stream().filter(bo -> bo.getUsername().equals(username)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("Collaborateur avec le username [%s] deja existe!", username))
        );
        collaborateurToPersist.setId(collaborateurFound.getId());
        collaborateurToPersist.setUsername(username);
        UpdateCollaborateurResponse updateCollaborateurResponse = modelMapper.map(collaborateurRepository.save(collaborateurToPersist), UpdateCollaborateurResponse.class);
        updateCollaborateurResponse.setMessage(String.format("Collaborateur avec username [%s] a ete modifie avec succes !", username));
        return updateCollaborateurResponse;
    }

    @Override
    public List<CollaborateurDTO> findByUsernameContaining(String searchTerm) {
        return collaborateurRepository.findByUsernameContainingIgnoreCase(searchTerm.toLowerCase()).stream().
                map(collaborateur -> modelMapper.map(collaborateur, CollaborateurDTO.class)).
                collect(Collectors.toList());
    }




}
