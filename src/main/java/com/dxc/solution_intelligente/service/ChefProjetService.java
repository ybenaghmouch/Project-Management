package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ChefProjetRepository;
import com.dxc.solution_intelligente.DAO.ChefProjetRepository;
import com.dxc.solution_intelligente.DTO.chefProjet.*;
import com.dxc.solution_intelligente.DTO.chefProjet.*;
import com.dxc.solution_intelligente.DTO.chefProjet.ChefProjetDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.ChefProjet;
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
public class ChefProjetService implements IChefProjetService{

    private final ChefProjetRepository chefProjetRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;





    @Override
    public List<ChefProjetDTO> getAllChefProjets() {
        return chefProjetRepository.findAll().stream().
                map(chefProjet -> modelMapper.map(chefProjet, ChefProjetDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddChefProjetResponse createChefProjet(AddChefProjetRequest addChefProjetRequest) {
        ChefProjet bo = modelMapper.map(addChefProjetRequest, ChefProjet.class);
        String username = bo.getUsername();
        //System.out.println("password 1= "+ addChefProjetRequest.toString());
        bo.setPassword(passwordEncoder.encode(bo.getPassword()));
        //System.out.println("password 2= "+ bo.getPassword());
        chefProjetRepository.findByUsername(username).ifPresent(a ->{
                    throw new BusinessException(String.format("ChefProjet avec le meme username [%s] existe", username));

                }
        );
        AddChefProjetResponse response = modelMapper.map(chefProjetRepository.save(bo), AddChefProjetResponse.class);
        response.setMessage(String.format("ChefProjet : [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Specilite = %s]", response.getFirstName(), response.getLastName(), response.getUsername(), response.getEmail(), response.getCivility(), response.getSpeciality()));
        return response;
    }

    @Override
    public UpdateChefProjetResponse updateChefProjet(String username, UpdateChefProjetRequest updateChefProjetRequest) {
        ChefProjet chefProjetToPersist = modelMapper.map(updateChefProjetRequest, ChefProjet.class);
        ChefProjet chefProjetFound = chefProjetRepository.findAll().stream().filter(bo -> bo.getUsername().equals(username)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("ChefProjet avec le username [%s] deja existe!", username))
        );
        chefProjetToPersist.setId(chefProjetFound.getId());
        chefProjetToPersist.setUsername(username);
        UpdateChefProjetResponse updateChefProjetResponse = modelMapper.map(chefProjetRepository.save(chefProjetToPersist), UpdateChefProjetResponse.class);
        updateChefProjetResponse.setMessage(String.format("ChefProjet avec username [%s] a ete modifie avec succes !", username));
        return updateChefProjetResponse;
    }



    @Override
    public List<ChefProjetDTO> findByUsernameContaining(String searchTerm) {
        return chefProjetRepository.findByUsernameContainingIgnoreCase(searchTerm.toLowerCase()).stream().
                map(chefProjet -> modelMapper.map(chefProjet, ChefProjetDTO.class)).
                collect(Collectors.toList());
    }
    
    
    
    
    
   
}
