package com.example.userCrud.Service;

import com.example.userCrud.Dto.LeaveApprovalReq;
import com.example.userCrud.Dto.LeaveApprovalRes;
import com.example.userCrud.Dto.LeaveRequestRes;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.LeaveApproval;
import com.example.userCrud.Entity.LeaveRequest;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.LeaveApprovalRepository;
import com.example.userCrud.Repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaveApprovalService {

    @Autowired
    ValidationService validationService;

    @Autowired
    LeaveApprovalRepository leaveApprovalRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    @Transactional
    public LeaveApprovalRes create(LeaveApprovalReq request){

        validationService.validate(request);

        LeaveRequest leaveRequest = leaveRequestRepository.findFirstById(request.getIdRequest())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        EmployeeEntity approver = employeeRepository.findByName(request.getNamaApproval())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Approval not found"));

        LeaveApproval approval = new LeaveApproval();

        approval.setLeaveRequest(leaveRequest);
        approval.setApprover(approver);

        leaveRequest.getApprovers().add(approval);
        leaveRequestRepository.save(leaveRequest);

        leaveApprovalRepository.save(approval);

        return toApprovalResponse(approval);

    }

    private LeaveApprovalRes toApprovalResponse(LeaveApproval approvalreq){

        LeaveApproval approval1 = approvalreq;

        return LeaveApprovalRes.builder()
                .id(approval1.getId())
                .idRequest(approval1.getLeaveRequest().getId())
                .namaApproval(approval1.getApprover().getName())
                .status(approval1.getStatus())
                .comment(approval1.getComment())
                .build();
    }
}
