package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Repository.CompanyCalendarRepository;
import com.example.userCrud.Service.CompanyCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyCalendarController {

    @Autowired
    CompanyCalendarService companyCalendarService;

    @PostMapping(
            path = "/api/addCompanyCalendar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyCalendarRes> create(@RequestBody CompanyCalendarReq request) {
        CompanyCalendarRes companyCalendarRes = companyCalendarService.create(request);
        return web_response.<CompanyCalendarRes>builder().data(companyCalendarRes).build();
    }

}
