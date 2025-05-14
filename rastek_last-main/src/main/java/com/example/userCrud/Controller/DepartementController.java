package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Service.DepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartementController {
    private static final String COMPANY_DEPARTMENT_ADD = "COMPANY:DEPARTMENT:ADD";
    private static final String COMPANY_DEPARTMENT_VIEW = "COMPANY:DEPARTMENT:VIEW";
    private static final String COMPANY_DEPARTMENT_EDIT = "COMPANY:DEPARTMENT:EDIT";

    @Autowired
    private DepartementService departementService;


    //Post API
    @PreAuthorize("hasAuthority('" + COMPANY_DEPARTMENT_ADD + "')")
    @PostMapping(
            path = "/api/addDepartement",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DepartementRes> create(@RequestBody DepartementReq request) {
        DepartementRes departementRes = departementService.create(request);
        return web_response.<DepartementRes>builder().data(departementRes).build();
    }

    //Get API
    @PreAuthorize("hasAuthority('" + COMPANY_DEPARTMENT_VIEW + "')")
    @GetMapping(
            path = "/api/addDepartement/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DepartementRes> get(@PathVariable("id") Long id){
        DepartementRes departementRes = departementService.get(id);
        return web_response.<DepartementRes>builder().data(departementRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_DEPARTMENT_VIEW + "')")
    @GetMapping(
            path = "/api/getDepartement",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DepartementRes> getAll(){
        return departementService.getAllDepartement();
    }

    //Put API
    @PreAuthorize("hasAuthority('" + COMPANY_DEPARTMENT_EDIT + "')")
    @PutMapping(
            path = "/api/addDepartement/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DepartementRes> update(@RequestBody UpdateDepartementReq request,
                                           @PathVariable("id") Long id) {
        request.setId(id);
        DepartementRes departementRes = departementService.update(request);
        return web_response.<DepartementRes>builder().data(departementRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_DEPARTMENT_EDIT + "')")
    @DeleteMapping(
            path = "/api/deleteDepartement/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("id") Long id){
        departementService.delete(id);
        return web_response.<String>builder().data("Data Deleted").build();
    }
}
