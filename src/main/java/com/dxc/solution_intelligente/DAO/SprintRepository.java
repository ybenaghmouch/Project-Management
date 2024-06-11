package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    Optional<Sprint> findSprintByTitre(String titre);
    List<Sprint> findByTitreContainingIgnoreCase(String titre);
}
