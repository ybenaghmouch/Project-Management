package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.ChefProjet;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChefProjetDAO extends JpaRepository<ChefProjet,Long> {
    Optional<Collaborateur> findByUsername(String username);
}
