package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.RiwayatJabatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RiwayatJabatanController {
    private static final String EMPLOYEES_VIEW = "EMPLOYEES:VIEW";
    private static final String COMPANY_JOB_HISTORY_VIEW = "COMPANY:JOB HISTORY:VIEW";
    private static final String COMPANY_JOB_HISTORY_ADD = "COMPANY:JOB HISTORY:ADD";
    private static final String COMPANY_JOB_HISTORY_EDIT = "COMPANY:JOB HISTORY:EDIT";
    private static final String EMPLOYEES_DETAILED_VIEW = "EMPLOYEES:DETAILED:VIEW";
    private static final String EMPLOYEES_SELF_VIEW = "EMPLOYEES:SELF:VIEW";

    @Autowired
    private RiwayatJabatanService riwayatJabatanService;

    @PreAuthorize("hasAuthority('" + COMPANY_JOB_HISTORY_ADD + "')")
    @PostMapping(
            path = "/api/addRiwayatJabatan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<RiwayatJabatanRes> create(@RequestBody RiwayatJabatanReq request) {
        RiwayatJabatanRes riwayatJabatanRes = riwayatJabatanService.create(request);
        return web_response.<RiwayatJabatanRes>builder().data(riwayatJabatanRes).build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "') || hasAuthority('" + EMPLOYEES_DETAILED_VIEW + "') " +
            "|| hasAuthority('" + EMPLOYEES_SELF_VIEW + "') || hasAuthority('" + COMPANY_JOB_HISTORY_VIEW + "')")
    @GetMapping(
            path = "/api/getRiwayatJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<RiwayatJabatanRes> get(@PathVariable("id") Long id){
        RiwayatJabatanRes riwayatJabatanRes = riwayatJabatanService.get(id);
        return web_response.<RiwayatJabatanRes>builder().data(riwayatJabatanRes).build();
    }

    @PreAuthorize("hasAuthority('" + EMPLOYEES_VIEW + "') || hasAuthority('" + EMPLOYEES_DETAILED_VIEW + "') " +
            "|| hasAuthority('" + EMPLOYEES_SELF_VIEW + "') || hasAuthority('" + COMPANY_JOB_HISTORY_VIEW + "')")
    @GetMapping(
            path = "/api/getRiwayatJabatan",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<RiwayatJabatanRes> getAll(){
        return riwayatJabatanService.getAll();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_JOB_HISTORY_EDIT + "')")
    @PutMapping(
            path = "/api/addRiwayatJabatan/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<RiwayatJabatanRes> update(@RequestBody UpdateRiwayatJabatanReq request,
                                            @PathVariable("id") Long id) {
        request.setId(id);
        RiwayatJabatanRes riwayatJabatanRes = riwayatJabatanService.update(request);
        return web_response.<RiwayatJabatanRes>builder().data(riwayatJabatanRes).build();
    }

    @PreAuthorize("hasAuthority('" + COMPANY_JOB_HISTORY_EDIT + "')")
    @DeleteMapping(
            path = "/api/deleteRiwayatJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("id") Long id){
        riwayatJabatanService.delete(id);
        return web_response.<String>builder().data("Data Deleted").build();
    }

}
