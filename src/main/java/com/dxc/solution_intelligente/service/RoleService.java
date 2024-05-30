package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.PermissionRepository;
import com.dxc.solution_intelligente.DAO.RoleRepository;
import com.dxc.solution_intelligente.DTO.Role.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().
                map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
    }
    @Override
    public AddRoleResponse createRole(AddRoleRequest addRoleRequest) {
        Role bo = modelMapper.map(addRoleRequest, Role.class);
        String authority = bo.getAuthority();
        roleRepository.findRoleByAuthority(authority).ifPresent(a->
        {
            throw new BusinessException(String.format("Role deja existe [%s]", authority));
        });
        AddRoleResponse response = modelMapper.map(roleRepository.save(bo), AddRoleResponse.class);
        response.setMessage(String.format("Role : [Authority = %s]", response.getAuthority()));
        /*Permission permission = permissionRepository.findByAuthority(response.getAuthority())
                .orElseThrow(() -> new BusinessException("Permission not found"));
        response.set*/
        return response;
    }

   /* @Override
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
    }*/

    @Override
    public UpdateRoleResponse updateRole(String authority, UpdateRoleRequest updateRoleRequest) {
        Role roleToPersist = modelMapper.map(updateRoleRequest, Role.class);
        Role roleFound = roleRepository.findAll().stream().filter(bo -> bo.getAuthority().equals(authority)).findFirst().orElseThrow(
                () -> new BusinessException(String.format("Role avec le authority [%s] deja existe!", authority))
        );
        roleToPersist.setId(roleFound.getId());
        roleToPersist.setAuthority(authority);
        UpdateRoleResponse updateRoleResponse = modelMapper.map(roleRepository.save(roleToPersist), UpdateRoleResponse.class);
        updateRoleResponse.setMessage(String.format("Role avec authority [%s] a ete modifie avec succes !", authority));
        return updateRoleResponse;
    }

}
