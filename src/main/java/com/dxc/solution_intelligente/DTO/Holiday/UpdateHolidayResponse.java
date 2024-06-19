package com.dxc.solution_intelligente.DTO.Holiday;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class UpdateHolidayResponse {
    private Long id;
    private String name;
    private int duration;

    private Date startDate;
    private Date endDate;
    private boolean isAnnual;
    private String message;
}
