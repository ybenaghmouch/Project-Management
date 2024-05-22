package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Equipe;
import com.dxc.solution_intelligente.service.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TacheRepository extends JpaRepository<Tache,Long> {
    Optional<Tache> findBycode(String name);
    List<Tache> findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(String code, String titre);
}
