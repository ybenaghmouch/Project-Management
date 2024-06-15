package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Backlog;
import com.dxc.solution_intelligente.service.model.Conge;
import com.dxc.solution_intelligente.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CongeRepository extends JpaRepository<Conge,Long > {
   Optional<Conge> findCongeById(Long id);
   List<Conge> findByBackup(User backup);


}
