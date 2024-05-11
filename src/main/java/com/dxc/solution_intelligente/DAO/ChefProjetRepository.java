package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.ChefProjet;
import com.dxc.solution_intelligente.service.model.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ChefProjetRepository extends JpaRepository<ChefProjet,Long> {
    Optional<ChefProjet> findByUsername(String username);
    List<ChefProjet> findByUsernameContainingIgnoreCase(String username);

}
