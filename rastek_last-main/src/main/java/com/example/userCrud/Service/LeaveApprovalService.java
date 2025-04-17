package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    @Autowired
    private EmployeeEventService employeeEventService;

    @Autowired
    private EmployeeCalendarService employeeCalendarService;

    @Transactional
    public List<LeaveApprovalRes> create(LeaveApprovalReq request) {
        log.debug("Processing leave approval request for ID: {}", request.getIdRequest());
        validationService.validate(request);

        LeaveRequest leaveRequest = leaveRequestRepository.findFirstById(request.getIdRequest())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        List<EmployeeEntity> approvers = employeeRepository.findByNIKIn(request.getNikApproval());
        if (approvers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No valid approvers found");
        }

        LeaveRequest finalLeaveRequest = leaveRequest;
        List<LeaveApproval> approvals = approvers.stream().map(approver -> {
            LeaveApproval approval = new LeaveApproval();
            approval.setLeaveRequest(finalLeaveRequest);
            approval.setApprover(approver);
            approval.setStatus(request.getStatus());
            return approval;
        }).toList();

        // Save approvals first
        leaveRequest.getApprovers().addAll(approvals);
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        log.info("Checking approval status for request ID: {}", request.getIdRequest());

        if (isFullyApproved(leaveRequest)) {
            log.info("Request {} is fully approved, creating calendar entries", request.getIdRequest());
            createLeaveEventAndCalendar(leaveRequest);
        }

        return leaveRequest.getApprovers().stream()
                .map(this::toApprovalResponse)
                .collect(Collectors.toList());
    }

    private boolean isFullyApproved(LeaveRequest leaveRequest) {
        // Refresh the request to get latest approvals
        LeaveRequest refreshedRequest = leaveRequestRepository.findFirstById(leaveRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        boolean allApproved = refreshedRequest.getApprovers().stream()
                .allMatch(approval -> "Approved".equals(approval.getStatus()));

        log.info("Request {} approval status check: {}", leaveRequest.getId(), allApproved);
        return allApproved;
    }

    @Transactional
    private void createLeaveEventAndCalendar(LeaveRequest leaveRequest) {
        try {
            // Create Employee Event
            EmployeeEventReq eventReq = EmployeeEventReq.builder()
                    .eventName(leaveRequest.getJenisCuti().getJenisCuti()) // get company leave jenis cuti and set it to event name
                    .isCuti(true)
                    .build();

            log.info("Creating event for employee NIK: {}", leaveRequest.getEmployee().getNIK());

            EmployeeEventRes eventRes = employeeEventService.create(
                    leaveRequest.getEmployee().getNIK(),
                    eventReq
            );

            log.info("Event created with ID: {}", eventRes.getId());

            // Create Employee Calendar
            EmployeeCalendarReq calendarReq = EmployeeCalendarReq.builder()
                    .idEmployeeEvent(eventRes.getId())
                    .startDate(LocalDateTime.of(leaveRequest.getStartDate(), LocalTime.MIN))
                    .endDate(LocalDateTime.of(leaveRequest.getEndDate(), LocalTime.MAX))
                    .description(leaveRequest.getAlasan())
                    .build();

            employeeCalendarService.create(
                    leaveRequest.getEmployee().getNIK(),
                    calendarReq
            );

            // Update leave request status
            leaveRequest.setStatus("Approved");
            leaveRequestRepository.save(leaveRequest);

            log.info("Calendar entry created and request status updated for ID: {}", leaveRequest.getId());
        } catch (Exception e) {
            log.error("Error creating calendar entries: ", e);
            throw new RuntimeException("Failed to create calendar entries", e);
        }
    }


    private LeaveApprovalRes toApprovalResponse(LeaveApproval approvalreq) {
        LeaveApprovalRes response = new LeaveApprovalRes();
        response.setId(approvalreq.getId());
        response.setIdRequest(approvalreq.getLeaveRequest().getId());
        response.setNamaApproval(approvalreq.getApprover().getName());
        response.setStatus(approvalreq.getStatus());
        return response;
    }
}
