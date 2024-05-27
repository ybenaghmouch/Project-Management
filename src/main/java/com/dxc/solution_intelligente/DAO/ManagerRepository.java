package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Admin;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import com.dxc.solution_intelligente.service.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findManagerByUsername(String username);

    List<Manager> findByUsernameContainingIgnoreCase(String username);
}
