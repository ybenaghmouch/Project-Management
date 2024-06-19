package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DTO.Holiday.*;

import java.util.List;

public interface IHolidayService {
    List<HolidayDTO> getAllHolidays();
    HolidayDTO getHolidayById(Long id);
    AddHolidayResponse createHoliday(AddHolidayRequest holidayAddDTO);
    UpdateHolidayResponse updateHoliday(Long id, UpdateHolidayRequest holidayUpdateDTO);
    String deleteHoliday(Long id);
}
