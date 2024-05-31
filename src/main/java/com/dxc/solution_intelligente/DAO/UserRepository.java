package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT u FROM User u JOIN u.authorities r WHERE r.authority = :role AND LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<User> findByRoleAndUsernameContainingIgnoreCase(@Param("role") String role, @Param("username") String username);



}
