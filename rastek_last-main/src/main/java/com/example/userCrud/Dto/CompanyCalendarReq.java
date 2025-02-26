package com.example.userCrud.Dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCalendarReq {

    private Long idEvent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String description;

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
