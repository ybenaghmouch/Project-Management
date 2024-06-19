package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.HolidayRepository;
import com.dxc.solution_intelligente.DTO.Holiday.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Holiday;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class HolidayService implements IHolidayService{
    private final HolidayRepository holidayRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<HolidayDTO> getAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(holiday -> modelMapper.map(holiday, HolidayDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HolidayDTO getHolidayById(Long id) {
        return holidayRepository.findById(id)
                .map(holiday -> modelMapper.map(holiday, HolidayDTO.class))
                .orElseThrow(() -> new BusinessException(String.format("Holiday with ID [%s] not found!", id)));
    }

    @Override
    public AddHolidayResponse createHoliday(AddHolidayRequest addHolidayRequest) {
        Holiday holiday = modelMapper.map(addHolidayRequest, Holiday.class);

        // Check if the holiday already exists
        holidayRepository.findByName(holiday.getName()).stream()
                .filter(existingHoliday -> existingHoliday.getStartDate().equals(holiday.getStartDate()))
                .findFirst()
                .ifPresent(existingHoliday -> {
                    throw new BusinessException(String.format("Holiday with the same name [%s] and start date [%s] already exists", holiday.getName(), holiday.getStartDate()));
                });

        Holiday savedHoliday = holidayRepository.save(holiday);
        AddHolidayResponse response = modelMapper.map(savedHoliday, AddHolidayResponse.class);
        response.setMessage(String.format("Holiday: [Name = %s, Start Date = %s, End Date = %s, Duration = %d, Annual = %s]",
                response.getName(), response.getStartDate(), response.getEndDate(), response.getDuration(), response.isAnnual()));

        return response;
    }

    @Override
    public UpdateHolidayResponse updateHoliday(Long id, UpdateHolidayRequest updateHolidayRequest) {
        Holiday holidayFound = holidayRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("Holiday with ID [%s] not found!", id)));

        // Use ModelMapper to map non-null properties from updateHolidayRequest to holidayFound
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(updateHolidayRequest, holidayFound);

        Holiday savedHoliday = holidayRepository.save(holidayFound);

        UpdateHolidayResponse updateHolidayResponse = modelMapper.map(savedHoliday, UpdateHolidayResponse.class);
        updateHolidayResponse.setMessage(String.format("Holiday with ID [%s] was successfully updated!", id));

        return updateHolidayResponse;
    }

    @Override
    public String deleteHoliday(Long id) {
        if (holidayRepository.existsById(id)) {
            holidayRepository.deleteById(id);
            return String.format("Holiday with ID [%s] was successfully deleted!", id);
        } else {
            throw new BusinessException(String.format("Holiday with ID [%s] not found!", id));
        }
    }

    public int calculateLeaveDurationExcludingHolidaysAndWeekends(Date startDate, Date endDate) {
        // Convert Date to LocalDate
        LocalDate start = convertToLocalDate(startDate);
        LocalDate end = convertToLocalDate(endDate);

        // Fetch holidays between the given dates
        List<Holiday> holidays = holidayRepository.findAll().stream()
                .filter(holiday -> !holiday.getStartDate().toInstant().isAfter(end.atStartOfDay(ZoneId.systemDefault()).toInstant())
                        && !holiday.getEndDate().toInstant().isBefore(start.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .collect(Collectors.toList());

        int duration = 0;
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {
            if (!isHoliday(currentDate, holidays) && !isWeekend(currentDate)) {
                duration++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return duration;
    }

    private boolean isHoliday(LocalDate date, List<Holiday> holidays) {
        return holidays.stream().anyMatch(holiday ->
                !date.isBefore(convertToLocalDate(holiday.getStartDate())) &&
                        !date.isAfter(convertToLocalDate(holiday.getEndDate())));
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
