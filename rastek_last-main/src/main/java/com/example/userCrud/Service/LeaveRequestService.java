package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    JabatanRepository jabatanRepository;

    @Autowired
    LeaveApprovalRepository leaveApprovalRepository;

    @Autowired
    CompanyCalendarRepository companyCalendarRepository;

    @Autowired
    CompanyEventRepository companyEventRepository;


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

    private LeaveRequestRes toLeaveRequestResponse(LeaveRequest leave) {

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
                .nama(leave.getEmployee().getName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .alasan(leave.getAlasan())
                .status(leave.getStatus())
                .jenis(leave.getJenisCuti().getJenisCuti())
                .leaveApprovalRes(leaveApprovalResList)
                .build();
    }

    @Transactional
    public void processLeaveRequestApproval(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave Request not found"));

        List<LeaveApproval> approvals = leaveApprovalRepository.findByLeaveRequest(leaveRequest);

        boolean allApproved = approvals.stream().allMatch(a -> "Approved".equals(a.getStatus()));
        boolean anyNotApproved = approvals.stream().anyMatch(a -> "Not Approved".equals(a.getStatus()));

        if (anyNotApproved) {
            leaveRequest.setStatus("Not Approved");
        } else if (allApproved) {
            int leaveDays = calculateEffectiveLeaveDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            leaveRequest.setStatus("Approved");
            deductLeaveBalance(leaveRequest.getEmployee(), leaveDays);
        }
        leaveRequestRepository.save(leaveRequest);
    }

    private int calculateEffectiveLeaveDays(LocalDate startDate, LocalDate endDate) {
        int count = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isCompanyHoliday = companyCalendarRepository.existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
            boolean isFreeEvent = companyEventRepository.existsFreeEventOnDate(date);;

            if (!isWeekend && !isCompanyHoliday && !isFreeEvent) {
                count++;
            }
            date = date.plusDays(1);
        }
        return count;
    }



    private void deductLeaveBalance(EmployeeEntity employee, int days) {
        EmployeeAnnual leaveBalance = employeeAnnualRepository.findByEmployee(employee)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee leave balance not found"));

        if (leaveBalance.getSisaCuti() >= days) {
            leaveBalance.setSisaCuti(leaveBalance.getSisaCuti() - days);
            employeeAnnualRepository.save(leaveBalance);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient leave balance");
        }
    }
}
