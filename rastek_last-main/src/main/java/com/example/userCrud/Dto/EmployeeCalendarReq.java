package com.example.userCrud.Dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCalendarReq {
    private Long idEmployeeCalendar;

    private Long idEmployeeEvent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

    private Long NIK;

    @AssertTrue(message = "Start date must be greater than or equal to current date")
    public boolean isStartDateValid() {
        if (startDate == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime start = startDate.truncatedTo(ChronoUnit.MINUTES);
        return start.isEqual(now) || start.isAfter(now);
    }

    @AssertTrue(message = "End date must be greater than Start date")
    public boolean isEndDateValid() {
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }
}
