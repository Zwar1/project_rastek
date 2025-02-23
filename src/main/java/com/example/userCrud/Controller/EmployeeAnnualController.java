package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.EmployeeAnnualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeAnnualController {

    @Autowired
    EmployeeAnnualService employeeAnnualService;

    @PostMapping(
            path = "/api/addEmployeeAnnual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeAnnualRes> create(@RequestBody EmployeeAnnualReq request) {
        EmployeeAnnualRes employeeAnnualRes = employeeAnnualService.create(request);
        return web_response.<EmployeeAnnualRes>builder().data(employeeAnnualRes).build();
    }

    @GetMapping(
            path = "/api/getEmployeAnnual/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeAnnualRes> get(@PathVariable("id") Long id){
        EmployeeAnnualRes employeeAnnualRes = employeeAnnualService.get(id);
        return web_response.<EmployeeAnnualRes>builder().data(employeeAnnualRes).build();
    }
}
