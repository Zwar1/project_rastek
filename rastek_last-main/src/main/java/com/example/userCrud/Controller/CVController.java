package com.example.userCrud.Controller;

import com.example.userCrud.Dto.CVReq;
import com.example.userCrud.Dto.CVRes;
import com.example.userCrud.Dto.EmployeeCVRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CVController {
    private static final String AUTHORITY_ALL = "EMPLOYEES:CV:ALL";
    private static final String AUTHORITY_VIEW_ONLY = "EMPLOYEES:CV:VIEW";

    @Autowired
    private CVService cvService;

    @PreAuthorize("hasAuthority('" + AUTHORITY_ALL + "')")
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

    @PreAuthorize("hasAuthority('" + AUTHORITY_ALL + "') || hasAuthority('" + AUTHORITY_VIEW_ONLY + "')")
    @GetMapping(
            path = "/api/employee/{nik}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<EmployeeCVRes> getByEmployee(@PathVariable("nik") Long nik) {
        EmployeeCVRes response = cvService.getEmployeeCv(nik);
        return web_response.<EmployeeCVRes>builder().data(response).build();
    }

    @PreAuthorize("hasAuthority('" + AUTHORITY_ALL + "')")
    @DeleteMapping(
            path = "/api/employee/{nik}/deleteCV/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> deleteCV(@PathVariable("nik") Long nik, @PathVariable("id") Long id) {
        cvService.deleteCV(id);
        return web_response.<String>builder().data("CV with id " + id + " deleted successfully").build();
    }
}
