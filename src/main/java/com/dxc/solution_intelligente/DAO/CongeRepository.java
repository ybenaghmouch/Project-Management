package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Conge;
import com.dxc.solution_intelligente.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CongeRepository extends JpaRepository<Conge,Long > {
   //Optional<Conge> findCongeByTitre(String titre);
   //List<Conge> findByDemandeurOrBackup(User demandeur,  User backup);
}
