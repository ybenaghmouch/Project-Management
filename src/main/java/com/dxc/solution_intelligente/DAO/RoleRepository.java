package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByAuthority(String authority);
}
