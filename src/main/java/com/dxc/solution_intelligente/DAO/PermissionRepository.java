package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findPermissionByAuthority(String authority);
    Optional<Permission> findByAuthority(String authority);

}
