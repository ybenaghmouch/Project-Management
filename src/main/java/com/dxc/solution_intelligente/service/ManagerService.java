package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.ManagerRepository;
import com.dxc.solution_intelligente.DTO.Manager.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Manager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ManagerService implements IManagerService{

    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream().
                map(manager -> modelMapper.map(manager, ManagerDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddManagerResponse createManager(AddManagerRequest addManagerRequest) {
        Manager bo = modelMapper.map(addManagerRequest, Manager.class);
        String username = bo.getUsername();
        //bo.setPassword(passwordEncoder.encode(bo.getPassword()));
        managerRepository.findManagerByUsername(username).ifPresent(a->
        {
            throw new BusinessException(String.format("Manager deja existe avec le nom d'utilisateur [%s]", username));
        });
        AddManagerResponse response = modelMapper.map(managerRepository.save(bo), AddManagerResponse.class);
        response.setMessage(String.format("Manager : [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Specialite = %s]", response.getFirstName(), response.getLastName(), response.getUsername(), response.getEmail(), response.getCivility(), response.getSpeciality()));
        return response;
    }

    @Override
    public UpdateManagerResponse updateManager(String username, UpdateManagerRequest updateManagerRequest) {
        Manager managerToPersist = modelMapper.map(updateManagerRequest, Manager.class);
        Manager managerFound = managerRepository.findAll().stream().filter(bo -> bo.getUsername() != null && bo.getUsername().equals(username)).findFirst().orElseThrow(
                ()-> new BusinessException(String.format("Aucun Manager avec le nom d'utilisateur [%s] existe !", username))
        );
        managerToPersist.setId(managerFound.getId());
        managerFound.setUsername(username);
        UpdateManagerResponse updateManagerResponse = modelMapper.map(managerRepository.save(managerToPersist), UpdateManagerResponse.class);
        updateManagerResponse.setMessage(String.format("Manager avec le nom d'utilisateur %s modifié avec succes", username));
        return updateManagerResponse;
    }
}
