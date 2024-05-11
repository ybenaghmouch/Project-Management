package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Admin.*;

import java.util.List;

public interface IAdminService {
    List<AdminDTO> getAllAdmins();
    AddAdminResponse createAdmin(AddAdminRequest addAdminRequest);
    UpdateAdminResponse updateAdmin(String username, UpdateAdminRequest updateAdminRequest);
}
