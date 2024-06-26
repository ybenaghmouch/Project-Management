package com.dxc.solution_intelligente.service.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int duration;
    private Date startDate;
    private Date endDate;
    private boolean isAnnual;
    public void addOneYearToDates() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(this.getStartDate());
        calendar.add(Calendar.YEAR, 1);
        this.setStartDate(calendar.getTime());

        calendar.setTime(this.getEndDate());
        calendar.add(Calendar.YEAR, 1);
        this.setEndDate(calendar.getTime());
    }
}
