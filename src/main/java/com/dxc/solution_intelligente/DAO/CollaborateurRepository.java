package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Collaborateur;
import com.dxc.solution_intelligente.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CollaborateurRepository extends JpaRepository<Collaborateur,Long> {

  //  List<Collaborateur> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrUsernameIgnoreCaseContaining(String keyword);
    Optional<Collaborateur> findByUsername(String username);

}
