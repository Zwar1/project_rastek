package com.example.userCrud.Controller;

import com.example.userCrud.Dto.CVReq;
import com.example.userCrud.Dto.CVRes;
import com.example.userCrud.Dto.EmployeeCVRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Repository.CVRepository;
import com.example.userCrud.Service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CVController {
    @Autowired
    private CVService cvService;

    @PostMapping(
            path = "/api/employee/{nik}/addCV",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<CVRes> addCV(@PathVariable("nik") Long nik, @RequestBody CVReq request) {
        request.setNIK(nik);
        CVRes cvRes = cvService.addCV(nik, request);
        return web_response.<CVRes>builder().data(cvRes).build();
    }


    @GetMapping(
            path = "/api/employee/{nik}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeCVRes> getByEmployee(@PathVariable("nik") Long nik) {
        EmployeeCVRes response = cvService.getEmployeeCv(nik);
        return web_response.<EmployeeCVRes>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/api/employee/{nik}/deleteCV/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> deleteCV(@PathVariable("nik") Long nik, @PathVariable("id") Long id) {
        cvService.deleteCV(id);
        return web_response.<String>builder().data("CV with id " + id + " deleted successfully").build();
    }
}
