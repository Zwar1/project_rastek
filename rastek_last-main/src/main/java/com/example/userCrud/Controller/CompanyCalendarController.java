package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.CompanyCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyCalendarController {
    private static final String AUTHORITY_COMPANY_ALL = "CALENDAR:COMPANY CALENDAR:ALL";
    private static final String AUTHORITY_COMPANY_VIEW = "CALENDAR:COMPANY CALENDAR:VIEW";

    @Autowired
    CompanyCalendarService companyCalendarService;

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "')")
    @PostMapping(
            path = "/api/addCompanyCalendar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyCalendarRes> create(@RequestBody CompanyCalendarReq request) {
        CompanyCalendarRes companyCalendarRes = companyCalendarService.create(request);
        return web_response.<CompanyCalendarRes>builder().data(companyCalendarRes).build();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "') ||('" + AUTHORITY_COMPANY_VIEW + "') ")
    @GetMapping("/api/getCompanyCalendar")
    public List<CompanyCalendarRes> getAllCalendars() {
        return companyCalendarService.getAllCalendars();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "')")
    @PutMapping(
            path = "/api/updateEvent/{calendarId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyCalendarRes> update(@PathVariable Long calendarId,
                                                   @RequestBody CompanyCalendarEventReq req) {
        CompanyCalendarRes companyCalendarRes = companyCalendarService.updateCalendarEvent(calendarId, req);
        return web_response.<CompanyCalendarRes>builder().data(companyCalendarRes).build();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "')")
    @DeleteMapping("/api/deleteCalendar/{calendarId}")
    public void delete(@PathVariable Long calendarId) {
        companyCalendarService.deleteCalendar(calendarId);
    }
}
