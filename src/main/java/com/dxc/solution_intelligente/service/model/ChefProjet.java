package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name ="id")
public class ChefProjet extends Collaborateur{
}
