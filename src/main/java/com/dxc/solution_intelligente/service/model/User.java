package com.dxc.solution_intelligente.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String FirstName;
    protected String LastName;
    protected String username;
    protected String Email;
    protected String Password;
    protected boolean Force_change_password;
    protected String Civility;
    protected String Speciality;
    protected boolean Status;



}
