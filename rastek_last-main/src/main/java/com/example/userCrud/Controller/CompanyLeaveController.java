package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Service.CompanyLeaveService;
import com.example.userCrud.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyLeaveController {

    @Autowired
    CompanyLeaveService companyLeaveService;

    @PostMapping(
            path = "/api/addCompanyLeave",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyLeaveRes> create(@RequestBody CompanyLeaveReq request) {
        CompanyLeaveRes companyLeaveRes = companyLeaveService.create(request);
        return web_response.<CompanyLeaveRes>builder().data(companyLeaveRes).build();
    }

    @GetMapping(
            path = "/api/getJenisCuti"
    )
    public ResponseEntity<List<CompanyLeaveRes>> getAllJenisCuti() {
        List<CompanyLeaveRes> jenisCuti = companyLeaveService.getAllCompanyLeaveWithInfo();
        return ResponseEntity.ok(jenisCuti);
    }

    @GetMapping(
            path = "/api/getJenisCuti/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyLeaveRes> get(@PathVariable("id") Long id){
        CompanyLeaveRes companyLeaveRes = companyLeaveService.get(id);
        return web_response.<CompanyLeaveRes>builder().data(companyLeaveRes).build();
    }


}

