package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Conge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User Demandeur;
    @ManyToOne
    private User backup;
    private int Duration;
    private String motif;
    private Date FromDate;
    private Date EndDate;
    private String Status;
}
