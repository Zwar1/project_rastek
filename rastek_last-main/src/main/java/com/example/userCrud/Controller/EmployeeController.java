package com.example.userCrud.Controller;


import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(
            path = "/api/addEmployee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> create(@RequestBody EmployeeReq request) {
        EmployeeRes employeeRes = employeeService.create(request);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @GetMapping(
            path = "/api/getEmployee/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> get(@PathVariable("id") Long id){
        EmployeeRes employeeRes = employeeService.get(id);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @GetMapping(
            path = "/api/getEmployee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<EmployeeEntity> getAllEmployee(){
        return employeeService.getAllEmployee();
    }

    @GetMapping(
            path = "/api/getProfile/Information",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<UserResponse> getProfileInformation() {
        UserResponse userResponse = employeeService.getByUserAuth();
        return web_response.<UserResponse>builder().message("Success").data(userResponse).build();
    }


    @PutMapping(
            path = "/api/editEmployee/{NIK}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> update(@RequestBody UpdateEmployeeReq request,
                                           @PathVariable("NIK") Long NIK) {
        request.setNIK(NIK);
        EmployeeRes employeeRes = employeeService.update(request);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    // Edit data for employee
    @PutMapping(
            path = "/api/editEmployeeKaryawan/{NIK}", // Change to NIK to avoid MissingPathVariableException
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeRes> updateByEmployee(@RequestBody UpdateEmployeeKaryawanReq request,
                                                      @PathVariable("NIK") Long NIK) {
        request.setNIK(NIK);
        EmployeeRes employeeRes = employeeService.updateDataByEmployee(request);
        return web_response.<EmployeeRes>builder().data(employeeRes).build();
    }

    @PostMapping
    public ResponseEntity<EmployeeRes> createEmployee(@RequestBody EmployeeReq req) {
        // Panggil service untuk membuat karyawan baru
        EmployeeRes employeeRes = employeeService.create(req);

        // Mengembalikan ResponseEntity dengan status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRes);
    }

    @GetMapping("api/getNIK")
    public ResponseEntity<List<Long>> getAllEmployeeNIKs() {
        List<Long> niks = employeeService.getAllEmployeeNIKs();
        return ResponseEntity.ok(niks);
    }

    @GetMapping("api/getEmployeeName")
    public ResponseEntity<List<String>> getAllEmployeeName() {
        List<String> name = employeeService.getAllEmployeeName();
        return ResponseEntity.ok(name);
    }

    @DeleteMapping(
            path = "/api/deleteEmployee/{NIK}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("NIK") Long NIK){
        employeeService.delete(NIK);
        return web_response.<String>builder().data("Data Deleted").build();
    }

    @GetMapping(
            path = "/api/getAllEmployeeWithInfo",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeRes>> getAllEmployees() {
        List<EmployeeRes> employees = employeeService.getAllEmployeeWithInfo();
        return ResponseEntity.ok(employees);
    }

    @GetMapping(
            path = "/api/getLastEmployee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeRes> getLastCreatedEmployee() {
        EmployeeRes response = employeeService.getLastCreatedEmployee();
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/api/getEmployeeJPU",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeRes>> getEmployeeJPU() {
        List<EmployeeRes> employeeJPU = employeeService.getEmployeeWithJPU();
        return ResponseEntity.ok(employeeJPU);
    }
}

