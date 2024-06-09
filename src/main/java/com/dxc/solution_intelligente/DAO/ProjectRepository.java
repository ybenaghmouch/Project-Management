package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findProjectByNom(String name);
    List<Project> findByNomContainingIgnoreCase(String name);

    Optional<Project> findProjectByManagerUsername(String username);
}
