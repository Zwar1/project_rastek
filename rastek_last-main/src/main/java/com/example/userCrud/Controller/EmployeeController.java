package com.example.userCrud.Controller;


import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class EmployeeController {
    private static final String EMPLOYEES_VIEW = "EMPLOYEES:VIEW";
    private static final String EMPLOYEES_ADD = "EMPLOYEES:ADD";
    private static final String EMPLOYEES_EDIT = "EMPLOYEES:EDIT";
    private static final String EMPLOYEES_DELETE = "EMPLOYEES:DELETE";
    private static final String EMPLOYEES_DETAILED_VIEW = "EMPLOYEES:DETAILED:VIEW";
    private static final String EMPLOYEES_LIST_VIEW_ALL = "EMPLOYEES:ALL:VIEW";
    private static final String EMPLOYEES_SELF_VIEW = "EMPLOYEES:SELF:VIEW";

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("hasAuthority('" + EMPLOYEES_ADD + "')")
    @PostMapping(
            path = "/api/addEmployee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> create(@RequestBody EmployeeReq request) {
        EmployeeRes employeeRes = employeeService.create(request);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "')")
    @GetMapping(
            path = "/api/getEmployee/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> get(@PathVariable("id") Long id){
        EmployeeRes employeeRes = employeeService.get(id);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_LIST_VIEW_ALL + "')")
    @GetMapping(
            path = "/api/getEmployee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<EmployeeRes> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_SELF_VIEW + "')")
    @GetMapping(
            path = "/api/getProfile/Information",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<UserResponse> getProfileInformation() {
        UserResponse userResponse = employeeService.getByUserAuth();
        return web_response.<UserResponse>builder().message("Success").data(userResponse).build();
    }


//    @PutMapping(
//            path = "/api/editEmployee/{NIK}",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @PreAuthorize("hasAuthority('Employees:Edit')")
//    public web_response<EmployeeRes> update(@RequestBody UpdateEmployeeReq request,
//                                           @PathVariable("NIK") Long NIK) {
//        request.setNIK(NIK);
//        EmployeeRes employeeRes = employeeService.update(request);
//        return web_response.<EmployeeRes>builder().data(employeeRes).build();
//    }

    // Edit data for employee
    @PreAuthorize("hasAuthority('" + EMPLOYEES_EDIT + "')")
    @PutMapping(
            path = "/api/editEmployeeKaryawan/{NIK}", // Change to NIK to avoid MissingPathVariableException
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> updateEmployeeByNIK(@RequestBody UpdateEmployeeKaryawanReq request,
                                                      @PathVariable("NIK") Long NIK) {
        request.setNIK(NIK);
        EmployeeRes employeeRes = employeeService.updateDataByEmployee(request);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "')")
    @GetMapping("api/getNIK")
    public ResponseEntity<List<Long>> getAllEmployeeNIKs() {
        List<Long> niks = employeeService.getAllEmployeeNIKs();
        return ResponseEntity.ok(niks);
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "')")
    @GetMapping("api/getEmployeeName")
    public ResponseEntity<List<String>> getAllEmployeeName() {
        List<String> name = employeeService.getAllEmployeeName();
        return ResponseEntity.ok(name);
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_DELETE + "')")
    @DeleteMapping(
            path = "/api/deleteEmployee/{NIK}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("NIK") Long NIK){
        employeeService.delete(NIK);
        return web_response.<String>builder().data("Data Deleted").build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_DETAILED_VIEW + "')")
    @GetMapping(
            path = "/api/getAllEmployeeWithInfo",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeRes>> getAllEmployees() {
        List<EmployeeRes> employees = employeeService.getAllEmployeeWithInfo();
        return ResponseEntity.ok(employees);
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "')")
    @GetMapping(
            path = "/api/getLastEmployee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeRes> getLastCreatedEmployee() {
        EmployeeRes response = employeeService.getLastCreatedEmployee();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_DETAILED_VIEW + "') && hasAuthority('" + EMPLOYEES_VIEW + "')")
    @GetMapping(
            path = "/api/getEmployeeJPU",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeRes>> getEmployeeJPU() {
        List<EmployeeRes> employeeJPU = employeeService.getEmployeeWithJPU();
        return ResponseEntity.ok(employeeJPU);
    }

    @PutMapping("/{NIK}/attendance")
    public ResponseEntity<EmployeeRes> updateAttendance(
            @PathVariable Long NIK,
            @RequestParam LocalDateTime checkIn,
            @RequestParam LocalDateTime checkOut,
            @RequestParam String workLocation,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String attendanceStatus) {

        EmployeeRes updatedEmployee = employeeService.updateLastAttendance(
                NIK, checkIn, checkOut, workLocation, latitude, longitude, attendanceStatus
        );

        return ResponseEntity.ok(updatedEmployee);
    }
}

