package com.example.userCrud.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCalendarEventReq {
    private EmployeeCalendarReq employeeCalendarReq;
    private EmployeeEventReq employeeEventReq;
}
