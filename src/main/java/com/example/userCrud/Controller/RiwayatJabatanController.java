package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.RiwayatJabatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RiwayatJabatanController {

    @Autowired
    private RiwayatJabatanService riwayatJabatanService;

    @PostMapping(
            path = "/api/addRiwayatJabatan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<RiwayatJabatanRes> create(@RequestBody RiwayatJabatanReq request) {
        RiwayatJabatanRes riwayatJabatanRes = riwayatJabatanService.create(request);
        return web_response.<RiwayatJabatanRes>builder().data(riwayatJabatanRes).build();
    }


    @GetMapping(
            path = "/api/addRiwayatJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<RiwayatJabatanRes> get(@PathVariable("id") Long id){
        RiwayatJabatanRes riwayatJabatanRes = riwayatJabatanService.get(id);
        return web_response.<RiwayatJabatanRes>builder().data(riwayatJabatanRes).build();
    }

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

    @DeleteMapping(
            path = "/api/deleteRiwayatJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("id") Long id){
        riwayatJabatanService.delete(id);
        return web_response.<String>builder().data("Data Deleted").build();
    }

}
