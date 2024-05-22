package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.TacheRepository;
import com.dxc.solution_intelligente.DTO.Tache.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Tache;
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

public class TacheService implements ITacheService{

    private final TacheRepository tacheRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;




    @Override
    public List<TacheDTO> getAllTaches() {
        return tacheRepository.findAll().stream().
                map(tache -> modelMapper.map(tache, TacheDTO.class)).
                collect(Collectors.toList());
    }

    @Override
    public AddTacheResponse createTache(AddTacheRequest addTacheRequest) {
        Tache bo = modelMapper.map(addTacheRequest, Tache.class);
        String code = bo.getCode();
        System.out.println("password 1= "+ addTacheRequest.toString());
        //System.out.println("password 2= "+ bo.getPassword());
        tacheRepository.findBycode(code).ifPresent(a ->{
                    throw new BusinessException(String.format("Tache avec le meme code [%s] existe", code));

                }
        );
        AddTacheResponse response = modelMapper.map(tacheRepository.save(bo), AddTacheResponse.class);
        //response.setMessage(String.format("Tache : [code = %s, Prenom = %s, code = %s, Email = %s, Civility = %s, Specilite = %s]", response.getcode(), response.getCollaborateurs().toString(), response.getManager().toString(), response.getChefprojet().toString()));
        return response;
    }

    @Override
    public UpdateTacheResponse updateTache(String code, UpdateTacheRequest updateTacheRequest) {
        Tache tacheToPersist = modelMapper.map(updateTacheRequest, Tache.class);
        Tache tacheFound = tacheRepository.findAll().stream().filter(bo -> bo.getCode().equals(code)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("Tache avec le code [%s] deja existe!", code))
        );
        tacheToPersist.setId(tacheFound.getId());
        tacheToPersist.setCode(code);
        UpdateTacheResponse updateTacheResponse = modelMapper.map(tacheRepository.save(tacheToPersist), UpdateTacheResponse.class);
        updateTacheResponse.setMessage(String.format("Tache avec code [%s] a ete modifie avec succes !", code));
        return updateTacheResponse;
    }

    @Override
    public List<TacheDTO> findByCodeAndTitreContaining(String searchTerm) {
        return tacheRepository.findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(searchTerm.toLowerCase(),searchTerm.toLowerCase()).stream().
                map(tache -> modelMapper.map(tache, TacheDTO.class)).
                collect(Collectors.toList());
    }
}
