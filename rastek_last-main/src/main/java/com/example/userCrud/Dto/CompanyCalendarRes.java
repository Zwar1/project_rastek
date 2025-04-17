package com.example.userCrud.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCalendarRes {

    private Long idCalendar;

    private Long idEvent;

    private String nameEvent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isFree;

    private String description;
}
