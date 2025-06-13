package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.EmployeeCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('CALENDAR:PERSONAL CALENDAR:ALL')")
public class EmployeeCalendarController {

    @Autowired
    EmployeeCalendarService employeeCalendarService;

    @PostMapping(
            path = "/api/addEmployeeCalendar/{nik}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeCalendarRes> create(@PathVariable Long nik, @RequestBody EmployeeCalendarReq request) {
        EmployeeCalendarRes employeeCalendarRes = employeeCalendarService.create(nik, request);
        return web_response.<EmployeeCalendarRes>builder().data(employeeCalendarRes).build();
    }

    @GetMapping("/api/getEmployeelendar")
    public List<EmployeeCalendarRes> getAllCalendars() {
        return employeeCalendarService.getAllEmployeeCalendars();
    }

    @GetMapping("/api/getEmployeeCalendar/{nik}")
    public web_response<List<EmployeeCalendarRes>> getCalendarByNik(@PathVariable Long nik) {
        List<EmployeeCalendarRes> calendars = employeeCalendarService.getCalendarByNIK(nik);
        return web_response.<List<EmployeeCalendarRes>>builder()
                .data(calendars)
                .message("Success")
                .error(null)
                .build();
    }

    @PutMapping(
            path = "/api/updateEmployeeCalendar/{employeeCalendarId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeCalendarRes> update(@PathVariable Long employeeCalendarId,
                                                    @RequestBody EmployeeCalendarEventReq req) {
        EmployeeCalendarRes employeeCalendarRes = employeeCalendarService.updateEmployeeCalendarEvent(employeeCalendarId, req);
        return web_response.<EmployeeCalendarRes>builder().data(employeeCalendarRes).build();
    }

    @DeleteMapping("/api/deleteEmployeeCalendar/{calendarId}")
    public void delete(@PathVariable Long calendarId) {
        employeeCalendarService.deleteCalendar(calendarId);
    }
}