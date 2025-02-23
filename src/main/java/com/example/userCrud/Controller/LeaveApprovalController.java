package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.LeaveApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaveApprovalController {

    @Autowired
    LeaveApprovalService leaveApprovalService;

    @PostMapping(
            path = "/api/addApproval",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<LeaveApprovalRes> create(@RequestBody LeaveApprovalReq request) {
        LeaveApprovalRes leaveApprovalRes = leaveApprovalService.create(request);
        return web_response.<LeaveApprovalRes>builder().data(leaveApprovalRes).build();
    }
}
