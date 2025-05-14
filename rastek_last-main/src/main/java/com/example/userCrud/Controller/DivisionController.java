package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DivisionController {
    private static final String COMPANY_DIVISION_ADD = "COMPANY:DIVISION:ADD";
    private static final String COMPANY_DIVISION_VIEW = "COMPANY:DIVISION:VIEW";
    private static final String COMPANY_DIVISION_EDIT = "COMPANY:DIVISION:EDIT";

    @Autowired
    DivisionService divisionService;

    @PreAuthorize("hasAuthority('" + COMPANY_DIVISION_ADD + "')")
    @PostMapping(
            path = "/api/addDivision",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DivisionRes> create(@RequestBody DivisionReq request) {
        DivisionRes divisionRes = divisionService.create(request);
        return web_response.<DivisionRes>builder().data(divisionRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_DIVISION_VIEW + "')")
    @GetMapping(
            path = "/api/addDivision/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DivisionRes> get(@PathVariable("id") Long id){
        DivisionRes divisionRes = divisionService.get(id);
        return web_response.<DivisionRes>builder().data(divisionRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_DIVISION_VIEW + "')")
    @GetMapping(
            path = "/api/getDivision",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DivisionRes> getAll(){
        return divisionService.getAllDivision();
    }

    //Put API
    @PreAuthorize("hasAuthority('" + COMPANY_DIVISION_EDIT + "')")
    @PutMapping(
            path = "/api/addDivision/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<DivisionRes> update(@RequestBody UpdateDivisionReq request,
                                              @PathVariable("id") Long id) {
        request.setId(id);
        DivisionRes divisionRes = divisionService.update(request);
        return web_response.<DivisionRes>builder().data(divisionRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_DIVISION_EDIT + "')")
    @DeleteMapping(
            path = "/api/deleteDivision/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("id") Long id){
        divisionService.delete(id);
        return web_response.<String>builder().data("Data Deleted").build();
    }

}
