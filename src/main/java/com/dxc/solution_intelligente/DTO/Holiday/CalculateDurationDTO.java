package com.dxc.solution_intelligente.DTO.Holiday;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CalculateDurationDTO {
    private Date startDate;
    private Date endDate;
}
