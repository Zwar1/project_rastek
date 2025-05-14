package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.EmployeeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('CALENDAR:PERSONAL CALENDAR:ALL')")
public class EmployeeEventController {
    @Autowired
    private EmployeeEventService employeeEventService;

    @PostMapping(
            path = "/api/addEmployeeEvent/{nik}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeEventRes> create(@PathVariable Long nik, @RequestBody EmployeeEventReq request) {
        EmployeeEventRes employeeEventRes = employeeEventService.create(nik, request);
        return web_response.<EmployeeEventRes>builder().data(employeeEventRes).build();
    }

    @GetMapping(
            path = "/api/getEmployeeEvent/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeEventRes> get(@PathVariable("id") Long id){
        EmployeeEventRes employeeEventRes = employeeEventService.get(id);
        return web_response.<EmployeeEventRes>builder().data(employeeEventRes).build();
    }

    @GetMapping(
            path = "/api/getEmployeeEvent",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<EmployeeEventRes> getAll(){
        return employeeEventService.getAllEmployeeEvent();
    }

    @DeleteMapping("/api/deleteEmployeeEvent/{eventId}")
    public void delete(@PathVariable Long eventId) {
        employeeEventService.deleteEvent(eventId);
    }
}
