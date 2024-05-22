package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Backlog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BacklogRepository extends JpaRepository<Backlog, Long> {
    Optional<Backlog> findBacklogByTitre(String titre);
}
