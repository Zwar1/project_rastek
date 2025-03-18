package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCalendarRes {

    private Long idCalendar;

    private String nameEvent;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isFree;

    private String description;
    
}
