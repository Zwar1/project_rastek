package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    @Autowired
    ValidationService validationService;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyLeaveRepository companyLeaveRepository;

    @Autowired
    EmployeeAnnualRepository employeeAnnualRepository;

    @Autowired
    CompanyCalendarRepository companyCalendarRepository;

    @Autowired
    LeaveApprovalRepository leaveApprovalRepository;

    @Autowired
    UserRepository userRepository;


    @Transactional
    public LeaveRequestRes create(LeaveRequestReq request) {
        // Validasi request
        validationService.validate(request);

        // Ambil data karyawan berdasarkan NIK
        EmployeeEntity employee = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        // Ambil data annual leave berdasarkan ID
        List<EmployeeAnnual> annual = employeeAnnualRepository.findAllByEmployee(employee);

        if (annual.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jatah Cuti not found");
        }
        EmployeeAnnual annualLeave = annual.stream()
                .filter(a -> a.getCompanyLeave().getId().equals(request.getIdLeave()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti tidak ditemukan untuk employee ini"));


        CompanyLeave companyLeave = companyLeaveRepository.findById(request.getIdLeave())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti not found"));

        Integer remainingLeaveQuota = Optional.ofNullable(annualLeave.getSisaCuti()).orElse(0);
        if (remainingLeaveQuota <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient leave quota");
        }

        // Buat entitas LeaveRequest baru
        LeaveRequest leave = new LeaveRequest();
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setAlasan(request.getAlasan());
        leave.setLeaveAnnual(annualLeave);
        leave.setJenisCuti(companyLeave);
        leave.setEmployee(employee);

        // Tambahkan leave request ke dalam daftar karyawan
        employee.getLeaveRequests().add(leave);
        employeeRepository.save(employee);

        employeeAnnualRepository.save(annualLeave);

        // Simpan leave request
        leaveRequestRepository.save(leave);

        // Return response
        return toLeaveRequestResponse(leave);
    }

    @Transactional
    public LeaveRequestRes get(Long id){
        LeaveRequest leaveRequest = leaveRequestRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request Cuti Not Found"));

        return toLeaveRequestResponse(leaveRequest);

    }

    @Transactional(readOnly = true)
    public List<LeaveRequestRes> getAllRequestWithInfo() {
        List<LeaveRequest> leaveRequestRes = leaveRequestRepository.findAll();

        return leaveRequestRes.stream()
                .map(this::toLeaveRequestResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestRes> getAllLeaveByEmployee(){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your session expired"));

        if(user.is_deleted()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is deleted");
        }

        EmployeeEntity employee = user.getEmployee();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployee(employee);

        return leaveRequests.stream().map(this::toLeaveRequestResponse).collect(Collectors.toList());

    }

    public LeaveRequestRes toLeaveRequestResponse(LeaveRequest leave) {

        List<LeaveApprovalRes> leaveApprovalResList = leave.getApprovers().stream()
                .map(leaveApproval -> LeaveApprovalRes.builder()
                        .id(leaveApproval.getId())
                        .idRequest(leaveApproval.getId())
                        .namaApproval(leaveApproval.getApprover().getName())
                        .status(leaveApproval.getStatus())
                        .build())
                .toList();

        return LeaveRequestRes.builder()
                .id(leave.getId())
                .NIK(leave.getEmployee().getNIK())
                .nama(leave.getEmployee().getName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .alasan(leave.getAlasan())
                .status(leave.getStatus())
                .jenis(leave.getJenisCuti().getJenisCuti())
                .leaveApprovalRes(leaveApprovalResList)
                .build();
    }

    public void processLeaveRequest(LeaveRequest request1) {
        boolean allApproved = request1.getApprovers().stream()
                .allMatch(app -> "Approved".equals(app.getStatus()));
        boolean anyNotApproved = request1.getApprovers().stream()
                .anyMatch(app -> "Not Approved".equals(app.getStatus()));

        if (anyNotApproved) {
            request1.setStatus("Not Approved");
        } else if (allApproved) {
            request1.setStatus("Approved");
            int leaveDays = calculateLeaveDays(request1.getStartDate(), request1.getEndDate());
            request1.setJumlahCuti(leaveDays);

            EmployeeAnnual employeeAnnual = request1.getLeaveAnnual();
            if (employeeAnnual != null) {
                employeeAnnual.setSisaCuti(employeeAnnual.getSisaCuti() - leaveDays);
                employeeAnnualRepository.save(employeeAnnual);
            }
        }
        leaveRequestRepository.save(request1);
    }

    private int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        int count = 0;
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                boolean isFree = companyCalendarRepository.existsByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsFreeTrue(date, date);
                if (!isFree) {
                    count++;
                }
            }
            date = date.plusDays(1);
        }
        return count;
    }

    @Transactional
    public LeaveRequestRes updateLeaveApproval(LeaveApprovalProcess request) {
        LeaveRequest request1 = leaveRequestRepository.findFirstById(request.getRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        List<LeaveApproval> approvals = leaveApprovalRepository.findByLeaveRequestAndApprover_NIK(request1, request.getNikApprover());

        if (approvals.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
        }

        approvals.forEach(approval -> approval.setStatus(request.getStatus()));
        leaveApprovalRepository.saveAll(approvals);


        processLeaveRequest(request1);

        return toLeaveRequestResponse(request1);
    }
}
