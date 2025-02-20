package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.EmployeeAnnualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeAnnualController {

    @Autowired
    EmployeeAnnualService employeeAnnualService;

    @PostMapping(
            path = "/api/addEmployeeAnnual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<List<EmployeeAnnualListRes>> create(@RequestBody List<EmployeeAnnualListReq> requests) {
        List<EmployeeAnnualListRes> employeeAnnualResponses = employeeAnnualService.createMultiple(requests);
        return web_response.<List<EmployeeAnnualListRes>>builder().data(employeeAnnualResponses).build();
    }


    @GetMapping(
            path = "/api/getSisaCutiKaryawan/{nik}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeAnnualListRes> getByEmployeeId(@PathVariable("nik") Long nik) {
        EmployeeAnnualListRes employeeAnnualListRes = employeeAnnualService.getByEmployeeId(nik);
        return web_response.<EmployeeAnnualListRes>builder().data(employeeAnnualListRes).build();
    }


}
