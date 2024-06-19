package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Holiday.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.HolidayService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@AllArgsConstructor
public class HolidayController {
    private final HolidayService holidayService;

    @GetMapping()
    public List<HolidayDTO> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HolidayDTO> getHolidayById(@PathVariable Long id) {
        try {
            HolidayDTO holiday = holidayService.getHolidayById(id);
            return new ResponseEntity<>(holiday, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createHoliday(@RequestBody AddHolidayRequest dto) {
        try {
            AddHolidayResponse response = holidayService.createHoliday(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateHolidayResponse> updateHoliday(@PathVariable Long id, @RequestBody UpdateHolidayRequest dto) {
        try {
            UpdateHolidayResponse response = holidayService.updateHoliday(id, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHoliday(@PathVariable Long id) {
        try {
            String message = holidayService.deleteHoliday(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-duration")
    public ResponseEntity<Integer> calculateLeaveDuration(@RequestBody CalculateDurationDTO calculateDurationDTO) {
        try {
            int duration = holidayService.calculateLeaveDurationExcludingHolidaysAndWeekends(calculateDurationDTO.getStartDate(), calculateDurationDTO.getEndDate());
            return new ResponseEntity<>(duration, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
