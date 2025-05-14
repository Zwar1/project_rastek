package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.CompanyEventService;
import com.example.userCrud.Service.CompanyLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyEventController {
    private static final String AUTHORITY_COMPANY_ALL = "CALENDAR:COMPANY CALENDAR:ALL";
    private static final String AUTHORITY_COMPANY_VIEW = "CALENDAR:COMPANY CALENDAR:VIEW";

    @Autowired
    private CompanyEventService companyEventService;

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "')")
    @PostMapping(
            path = "/api/addCompanyEvent",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyEventRes> create(@RequestBody CompanyEventReq request) {
        CompanyEventRes companyEventRes = companyEventService.create(request);
        return web_response.<CompanyEventRes>builder().data(companyEventRes).build();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "') ||('" + AUTHORITY_COMPANY_VIEW + "') ")
    @GetMapping(
            path = "/api/getCompanyEvent/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyEventRes> get(@PathVariable("id") Long id){
        CompanyEventRes companyEventRes = companyEventService.get(id);
        return web_response.<CompanyEventRes>builder().data(companyEventRes).build();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "') ||('" + AUTHORITY_COMPANY_VIEW + "') ")
    @GetMapping(
            path = "/api/getCompanyEvent",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CompanyEventRes> getAll(){
        return companyEventService.getAllCompanyEvent();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_COMPANY_ALL + "')")
    @DeleteMapping("/api/deleteEvent/{eventId}")
    public void delete(@PathVariable Long eventId) {
        companyEventService.deleteEvent(eventId);
    }
}
