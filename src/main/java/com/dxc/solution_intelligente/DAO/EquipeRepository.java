package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EquipeRepository extends JpaRepository<Equipe,Long> {
    Optional<Equipe> findByNom(String name);
    List<Equipe> findByNomContainingIgnoreCase(String name);
}
