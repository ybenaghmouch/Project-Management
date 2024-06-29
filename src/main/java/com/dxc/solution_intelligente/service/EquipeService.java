package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.EquipeRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Equipe.*;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Equipe;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipeService implements IEquipeService {
    private final EquipeRepository equipeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public List<EquipeDTO> getAllEquipes() {
        return equipeRepository.findAll().stream()
                .map(equipe -> modelMapper.map(equipe, EquipeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddEquipeResponse createEquipe(AddEquipeRequest addEquipeRequest) {
        equipeRepository.findByNom(addEquipeRequest.getNom()).ifPresent(a -> {
            throw new BusinessException(String.format("Equipe avec le même nom [%s] existe déjà", addEquipeRequest.getNom()));
        });

        List<UserDTO> collaborateursDTO = new ArrayList<>();
        List<User> collaborateurs = new ArrayList<>();
        for (UserDTO collaborateurDTO : addEquipeRequest.getCollaborateurs()) {
            User collaborateur = userRepository.findById(collaborateurDTO.getId())
                    .orElseThrow(() -> new BusinessException("Collaborateur not found"));
            collaborateurs.add(collaborateur);
            collaborateursDTO.add(modelMapper.map(collaborateur, UserDTO.class));
        }
        addEquipeRequest.setCollaborateurs(collaborateursDTO);

        User chefProjet = userRepository.findById(addEquipeRequest.getChefprojet().getId())
                .orElseThrow(() -> new BusinessException("ChefProjet not found"));
        User manager = userRepository.findById(addEquipeRequest.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));

        addEquipeRequest.setManager(modelMapper.map(manager, UserDTO.class));
        addEquipeRequest.setChefprojet(modelMapper.map(chefProjet, UserDTO.class));

        Equipe equipe = new Equipe();
        System.out.println(addEquipeRequest.getChefprojet().getId());
        equipe.setChefprojet(chefProjet);
        equipe.setManager(manager);
        equipe.setNom(addEquipeRequest.getNom());
        equipe.setCollaborateurs(collaborateurs);
        Equipe savedEquipe = equipeRepository.save(equipe);
        AddEquipeResponse response = modelMapper.map(savedEquipe, AddEquipeResponse.class);
        response.setMessage(String.format("Equipe : [Nom = %s]", response.getNom()));

        return response;
    }

    @Override
    public UpdateEquipeResponse updateEquipe(String name, UpdateEquipeRequest updateEquipeRequest) {
        Equipe equipeFound = equipeRepository.findAll().stream()
                .filter(bo -> bo.getNom().equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Equipe avec le name [%s] n'existe pas!", name)));

        User chefProjet = userRepository.findById(updateEquipeRequest.getChefprojet().getId())
                .orElseThrow(() -> new BusinessException("ChefProjet not found"));
        User manager = userRepository.findById(updateEquipeRequest.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));

        List<UserDTO> collaborateursDTO = new ArrayList<>();
        List<User> collaborateurs = new ArrayList<>();
        for (UserDTO collaborateurDTO : updateEquipeRequest.getCollaborateurs()) {
            User collaborateur = userRepository.findById(collaborateurDTO.getId())
                    .orElseThrow(() -> new BusinessException("Collaborateur not found"));
            collaborateurs.add(collaborateur);
            collaborateursDTO.add(modelMapper.map(collaborateur, UserDTO.class));
        }
        updateEquipeRequest.setCollaborateurs(collaborateursDTO);

        updateEquipeRequest.setManager(modelMapper.map(manager, UserDTO.class));
        updateEquipeRequest.setChefprojet(modelMapper.map(chefProjet, UserDTO.class));

        Equipe equipe = new Equipe();
        equipe.setId(equipeFound.getId());
        equipe.setChefprojet(chefProjet);
        equipe.setManager(manager);
        equipe.setNom(updateEquipeRequest.getNom());
        equipe.setCollaborateurs(collaborateurs);

        Equipe savedEquipe = equipeRepository.save(equipe);
        UpdateEquipeResponse updateEquipeResponse = modelMapper.map(savedEquipe, UpdateEquipeResponse.class);
        updateEquipeResponse.setMessage(String.format("Equipe avec name [%s] a été modifiée avec succès !", name));

        return updateEquipeResponse;
    }

    @Override
    public List<EquipeDTO> findByNomContaining(String searchTerm) {
        return equipeRepository.findByNomContainingIgnoreCase(searchTerm.toLowerCase()).stream()
                .map(equipe -> modelMapper.map(equipe, EquipeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EquipeDTO findByNom(String searchTerm) {
        return modelMapper.map(equipeRepository.findByNom(searchTerm.toLowerCase()), EquipeDTO.class);
    }

    @Override
    public String deleteEquipeById(Long id) {
        if (id == null)
            throw new BusinessException("Enter a correct identity Equipe");
        Equipe equipeFound = equipeRepository.findAll().stream()
                .filter(equipe -> equipe.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException(String.format("Aucune équipe n'existe avec l'identité %d", id)));
        equipeRepository.delete(equipeFound);
        return String.format("Team with identity %d is deleted with success", id);
    }
}
