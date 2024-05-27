package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ChefProjetRepository;
import com.dxc.solution_intelligente.DAO.EquipeRepository;
import com.dxc.solution_intelligente.DAO.ManagerRepository;
import com.dxc.solution_intelligente.DTO.Equipe.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.ChefProjet;
import com.dxc.solution_intelligente.service.model.Equipe;
import com.dxc.solution_intelligente.service.model.Manager;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@AllArgsConstructor
public class EquipeService implements IEquipeService{
    private final EquipeRepository equipeRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;


    private ManagerRepository managerRepository;

    private ChefProjetRepository chefProjetRepository;


    @Override
    public List<EquipeDTO> getAllEquipes() {
        return equipeRepository.findAll().stream().
                map(equipe -> modelMapper.map(equipe, EquipeDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddEquipeResponse createEquipe(AddEquipeRequest addEquipeRequest) {
        Equipe bo = modelMapper.map(addEquipeRequest, Equipe.class);
        String name = bo.getNom();
        System.out.println("password 1= "+ addEquipeRequest.toString());
        //System.out.println("password 2= "+ bo.getPassword());
        equipeRepository.findByNom(name).ifPresent(a ->{
                    throw new BusinessException(String.format("Equipe avec le meme name [%s] existe", name));

                }
        );
        AddEquipeResponse response = modelMapper.map(equipeRepository.save(bo), AddEquipeResponse.class);
        System.out.println("gggg"+equipeRepository.save(bo) );
        response.setMessage(String.format("Equipe : [Nom = %s]", response.getNom() ));

        ChefProjet chefProjet = chefProjetRepository.findById(response.getChefprojet().getId())
                .orElseThrow(() -> new BusinessException("ChefProjet not found"));
        Manager manager = managerRepository.findById(response.getManager().getId())
                .orElseThrow(() -> new BusinessException("Manager not found"));
        response.setManager(manager);
        response.setChefprojet(chefProjet);
        return response;
    }

    @Override
    public UpdateEquipeResponse updateEquipe(String name, UpdateEquipeRequest updateEquipeRequest) {
        Equipe equipeToPersist = modelMapper.map(updateEquipeRequest, Equipe.class);
        Equipe equipeFound = equipeRepository.findAll().stream().filter(bo -> bo.getNom().equals(name)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("Equipe avec le name [%s] deja existe!", name))
        );
        equipeToPersist.setId(equipeFound.getId());
        equipeToPersist.setNom(name);
        UpdateEquipeResponse updateEquipeResponse = modelMapper.map(equipeRepository.save(equipeToPersist), UpdateEquipeResponse.class);
        updateEquipeResponse.setMessage(String.format("Equipe avec name [%s] a ete modifie avec succes !", name));
        return updateEquipeResponse;
    }

    @Override
    public List<EquipeDTO> findByNomContaining(String searchTerm) {
        return equipeRepository.findByNomContainingIgnoreCase(searchTerm.toLowerCase()).stream().
                map(equipe -> modelMapper.map(equipe, EquipeDTO.class)).
                collect(Collectors.toList());
    }
}
