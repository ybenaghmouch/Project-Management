package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUsername(String username);
}
