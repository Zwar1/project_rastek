package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.JabatanEntity;
import com.example.userCrud.Service.JabatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JabatanController {

    @Autowired
    JabatanService jabatanService;

    @PostMapping(
            path = "/api/addJabatan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<JabatanRes> create(@RequestBody JabatanReq request) {
        JabatanRes jabatanRes = jabatanService.create(request);
        return web_response.<JabatanRes>builder().data(jabatanRes).build();
    }

    @GetMapping(
            path = "/api/getKodeJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<JabatanRes> get(@PathVariable("id") Long id){
        JabatanRes jabatan = jabatanService.get(id);
        return web_response.<JabatanRes>builder().data(jabatan).build();
    }

    @PutMapping(
            path = "/api/addJabatan/{kodeJabatan}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<JabatanRes> update(@RequestBody UpdateJabatanReq request,
                                           @PathVariable("kodeJabatan") String kodeJabatan) {
        request.setKodeJabatan(kodeJabatan);
        JabatanRes jabatanRes = jabatanService.update(request);
        return web_response.<JabatanRes>builder().data(jabatanRes).build();
    }

    @DeleteMapping(
            path = "/api/deleteJabatan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> delete(@PathVariable("id") Long id){
        jabatanService.delete(id);
        return web_response.<String>builder().data("Data Deleted").build();
    }

    @GetMapping(
            path = "/api/getJabatan",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<JabatanRes>> getAllJabatan() {
        List<JabatanRes> jabatanList = jabatanService.getAllJabatan();
        return new ResponseEntity<>(jabatanList, HttpStatus.OK);
    }

    @GetMapping(
            path = "/api/getKodeJabatan",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<String>> getAllKodeJabatan() {
        List<String> kodeJabatan = jabatanService.getAllKodeJabatan();
        return ResponseEntity.ok(kodeJabatan);
    }

    @GetMapping(
            path = "/api/getNamaJabatan",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<String>> getAllNamaJabatan() {
        List<String> namaJabatan = jabatanService.getAllNamaJabatan();
        return ResponseEntity.ok(namaJabatan);
    }

    @GetMapping("/api/getJabatanOnDepartement/{NIK}")
    public ResponseEntity<List<String>> getNamaJabatanAtasan(@PathVariable("NIK") Long NIK) {
        List<String> namaJabatan = jabatanService.getNamaJabatanAtasanInSameDepartement(NIK);
        return ResponseEntity.ok(namaJabatan);
    }
}
