package com.example.userCrud.Service;

import com.example.userCrud.Dto.LeaveApprovalProcess;
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

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    LeaveRequestService leaveRequestService;

    @Transactional
    public List<LeaveApprovalRes> create(LeaveApprovalReq request){

        validationService.validate(request);

        LeaveRequest leaveRequest = leaveRequestRepository.findFirstById(request.getIdRequest())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        // Cari semua approver berdasarkan list NIK
        List<EmployeeEntity> approvers = employeeRepository.findByNIKIn(request.getNikApproval());

        if (approvers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No valid approvers found");
        }

        List<LeaveApproval> approvals = approvers.stream().map(approver -> {
            LeaveApproval approval = new LeaveApproval();
            approval.setLeaveRequest(leaveRequest);
            approval.setApprover(approver);
            return approval;
        }).collect(Collectors.toList());

        // Simpan ke LeaveRequest
        leaveRequest.getApprovers().addAll(approvals);
        leaveRequestRepository.save(leaveRequest);

        // Simpan daftar LeaveApproval
        leaveApprovalRepository.saveAll(approvals);

        // Konversi ke response DTO
        return approvals.stream().map(this::toApprovalResponse).collect(Collectors.toList());

    }



    private LeaveApprovalRes toApprovalResponse(LeaveApproval approvalreq){
        LeaveApprovalRes response = new LeaveApprovalRes();
        response.setId(approvalreq.getId());
        response.setIdRequest(approvalreq.getLeaveRequest().getId());
        response.setNamaApproval(approvalreq.getApprover().getName());
        response.setStatus(approvalreq.getStatus());
        return response;
    }
}
