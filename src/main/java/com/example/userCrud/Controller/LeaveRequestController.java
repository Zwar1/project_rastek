package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LeaveRequestController {

    @Autowired
    LeaveRequestService leaveRequestService;

    @PostMapping(
            path = "/api/addLeaveRequest",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<LeaveRequestRes> create(@RequestBody LeaveRequestReq request) {
        LeaveRequestRes leaveRequestRes = leaveRequestService.create(request);
        return web_response.<LeaveRequestRes>builder().data(leaveRequestRes).build();
    }

    @GetMapping(
            path = "/api/getLeaveRequest/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<LeaveRequestRes> get(@PathVariable("id") Long id){
        LeaveRequestRes leaveRequestRes = leaveRequestService.get(id);
        return web_response.<LeaveRequestRes>builder().data(leaveRequestRes).build();
    }

    @GetMapping(
            path = "/api/getAllRequestWithInfo",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LeaveRequestRes>> getAllRequest() {
        List<LeaveRequestRes> leaveRequestRes = leaveRequestService.getAllRequestWithInfo();
        return ResponseEntity.ok(leaveRequestRes);
    }
}
