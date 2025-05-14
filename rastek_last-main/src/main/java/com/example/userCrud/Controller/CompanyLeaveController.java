package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Service.CompanyLeaveService;
import com.example.userCrud.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyLeaveController {
    private static final String LEAVES_LEAVE_TYPE_ADD = "LEAVES:LEAVE TYPE:ADD";
    private static final String LEAVES_LEAVE_TYPE_EDIT = "LEAVES:LEAVE TYPE:EDIT";
    private static final String LEAVES_LEAVE_TYPE_VIEW = "LEAVES:LEAVE TYPE:VIEW";

    @Autowired
    CompanyLeaveService companyLeaveService;

    @PreAuthorize("hasAuthority('" + LEAVES_LEAVE_TYPE_ADD + "')")
    @PostMapping(
            path = "/api/addCompanyLeave",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyLeaveRes> create(@RequestBody CompanyLeaveReq request) {
        CompanyLeaveRes companyLeaveRes = companyLeaveService.create(request);
        return web_response.<CompanyLeaveRes>builder().data(companyLeaveRes).build();
    }

    @PreAuthorize("hasAuthority('" + LEAVES_LEAVE_TYPE_VIEW + "')")
    @GetMapping(
            path = "/api/getJenisCuti"
    )
    public ResponseEntity<List<CompanyLeaveRes>> getAllJenisCuti() {
        List<CompanyLeaveRes> jenisCuti = companyLeaveService.getAllCompanyLeaveWithInfo();
        return ResponseEntity.ok(jenisCuti);
    }

    @PreAuthorize("hasAuthority('" + LEAVES_LEAVE_TYPE_VIEW + "')")
    @GetMapping(
            path = "/api/getJenisCuti/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyLeaveRes> get(@PathVariable("id") Long id){
        CompanyLeaveRes companyLeaveRes = companyLeaveService.get(id);
        return web_response.<CompanyLeaveRes>builder().data(companyLeaveRes).build();
    }

    @PreAuthorize("hasAuthority('" + LEAVES_LEAVE_TYPE_EDIT + "')")
    @PutMapping(
            path = "/api/updateJenisCuti/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CompanyLeaveRes> update(@PathVariable("id") Long id, @RequestBody CompanyLeaveReq request) {
        CompanyLeaveRes companyLeaveRes = companyLeaveService.update(id, request);
        return web_response.<CompanyLeaveRes>builder().data(companyLeaveRes).build();
    }
}

