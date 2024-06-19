package com.dxc.solution_intelligente.DAO;

import com.dxc.solution_intelligente.service.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    List<Holiday> findByName(String name);
}
