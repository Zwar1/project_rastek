package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.LeaveApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LeaveApprovalController {
    private static final String LEAVES_EMPLOYEE_LEAVE_ADD = "LEAVES:EMPLOYEE LEAVE:ADD";
    private static final String LEAVES_APPROVAL_ADD = "LEAVES:APPROVAL:ADD";

    @Autowired
    LeaveApprovalService leaveApprovalService;

    @PreAuthorize("hasAuthority('" + LEAVES_EMPLOYEE_LEAVE_ADD + "') || hasAuthority('" + LEAVES_APPROVAL_ADD + "')")
    @PostMapping(
            path = "/api/addApproval",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<web_response<List<LeaveApprovalRes>>> create(@RequestBody LeaveApprovalReq request) {
        List<LeaveApprovalRes> leaveApprovalResList = leaveApprovalService.create(request);
        return ResponseEntity.ok(web_response.<List<LeaveApprovalRes>>builder().data(leaveApprovalResList).build());
    }


}
