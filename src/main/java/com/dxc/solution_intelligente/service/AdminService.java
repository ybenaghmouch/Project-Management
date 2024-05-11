package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.AdminRepository;
import com.dxc.solution_intelligente.DTO.Admin.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Admin;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminService implements IAdminService{

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream().
                map(admin -> modelMapper.map(admin, AdminDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AddAdminResponse createAdmin(AddAdminRequest addAdminRequest) {
        Admin bo = modelMapper.map(addAdminRequest, Admin.class);
        String username = bo.getUsername();
        //bo.setPassword(passwordEncoder.encode(bo.getPassword()));
        adminRepository.findAdminByUsername(username).ifPresent(a->
        {
            throw new BusinessException(String.format("Admin deja existe avec le nom d'utilisateur [%s]", username));
        });
        AddAdminResponse response = modelMapper.map(adminRepository.save(bo), AddAdminResponse.class);
        response.setMessage(String.format("Admin : [Nom = %s, Prenom = %s, Username = %s, Email = %s, Civility = %s, Specialite = %s]", response.getFirstName(), response.getLastName(), response.getUsername(), response.getEmail(), response.getCivility(), response.getSpeciality()));
        return response;
    }

    @Override
    public UpdateAdminResponse updateAdmin(String username, UpdateAdminRequest updateAdminRequest) {
        Admin adminToPersist = modelMapper.map(updateAdminRequest, Admin.class);
        Admin adminFound = adminRepository.findAll().stream().filter(bo -> bo.getUsername() != null && bo.getUsername().equals(username)).findFirst().orElseThrow(
                ()-> new BusinessException(String.format("Aucun Admin avec le nom d'utilisateur [%s] existe !", username))
        );
        adminToPersist.setId(adminFound.getId());
        adminFound.setUsername(username);
        UpdateAdminResponse updateAdminResponse = modelMapper.map(adminRepository.save(adminToPersist), UpdateAdminResponse.class);
        updateAdminResponse.setMessage(String.format("Admin avec le nom d'utilisateur %s modifié avec succes", username));
        return updateAdminResponse;
    }
}
