package com.example.userCrud.Controller;

import com.example.userCrud.Dto.LeadsReq;
import com.example.userCrud.Dto.LeadsRes;
import com.example.userCrud.Dto.UserResponse;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.LeadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
public class LeadsController {

    @Autowired
    private LeadsService leadsService;

    @PostMapping(path = "/api/create/leads",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<LeadsRes> Create(@RequestBody LeadsReq req){
        LeadsRes leadsRes = leadsService.CreateLeads(req);
        return web_response.<LeadsRes>builder().data(leadsRes).message("Success").build();
    }

    @PatchMapping(
            path = "/api/update/accepted/{leadsId}"
    )
    public web_response<LeadsRes> UpdateToAccepted(@PathVariable("leadsId") Long id){
        LeadsRes leadsRes = leadsService.updateStatusToAccepted(id);
        return web_response.<LeadsRes>builder().data(leadsRes).message("Success").build();
    }

    @PatchMapping(
            path = "/api/update/rejected/{leadsId}"
    )
    public web_response<LeadsRes> UpdateToRejected(@PathVariable("leadsId") Long id){
        LeadsRes leadsRes = leadsService.updateStatusToRejected(id);
        return web_response.<LeadsRes>builder().data(leadsRes).message("Success").build();
    }

    @GetMapping(
            path = "/api/get/all/leads"
    )
    public List<LeadsRes> getAll(){
        return leadsService.getAllLeads();
    }

    @DeleteMapping(
            path = "/api/delete/leads/{id}"
    )
    public web_response<String> deleteLead(@PathVariable("id") Long id){
        leadsService.deleteLead(id);
        return web_response.<String>builder().data("Lead deleted successfully").message("Success").build();
    }
}
