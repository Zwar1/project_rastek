package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCalendarRes {
    private Long idEmployeeCalendar;

    private Long idEmployeeEvent;

    private String nameEvent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isCuti;

    private String description;

    private Long NIK;
}
