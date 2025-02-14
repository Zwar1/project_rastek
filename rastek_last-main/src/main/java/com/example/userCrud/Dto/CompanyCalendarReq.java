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
public class CompanyCalendarReq {

    private Long idEvent;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;
}
