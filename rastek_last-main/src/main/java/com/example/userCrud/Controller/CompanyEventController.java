package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.CompanyEventService;
import com.example.userCrud.Service.CompanyLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyEventController {

    @Autowired
    private CompanyEventService companyEventService;

    @PostMapping(
            path = "/api/addCompanyEvent",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyEventRes> create(@RequestBody CompanyEventReq request) {
        CompanyEventRes companyEventRes = companyEventService.create(request);
        return web_response.<CompanyEventRes>builder().data(companyEventRes).build();
    }

    @GetMapping(
            path = "/api/getCompanyEvent/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyEventRes> get(@PathVariable("id") Long id){
        CompanyEventRes companyEventRes = companyEventService.get(id);
        return web_response.<CompanyEventRes>builder().data(companyEventRes).build();
    }

    @GetMapping(
            path = "/api/getCompanyEvent",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CompanyEventRes> getAll(){
        return companyEventService.getAllCompanyEvent();
    }

}
