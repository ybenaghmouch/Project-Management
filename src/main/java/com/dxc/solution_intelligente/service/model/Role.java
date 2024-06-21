package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String authority;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Permission> authorities;

}
