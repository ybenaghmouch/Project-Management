package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Tache;
import com.dxc.solution_intelligente.service.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStoryRepository extends JpaRepository<UserStory,Long> {
    Optional<UserStory> findBycode(String name);
    List<UserStory> findByCodeContainingIgnoreCaseOrTitreContainingIgnoreCase(String code, String titre);
}
