package com.example.userCrud.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCalendarEventReq {
    private CompanyCalendarReq calendarReq;
    private CompanyEventReq eventReq;
}
