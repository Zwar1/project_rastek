package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
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


    @Transactional
    public LeaveRequestRes create(LeaveRequestReq request) {
        // Validasi request
        validationService.validate(request);

        // Ambil data karyawan berdasarkan NIK
        EmployeeEntity employee = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        // Ambil data annual leave berdasarkan ID
        EmployeeAnnual annual = employeeAnnualRepository.findFirstById(request.getIdAnnual())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nama Cuti not found"));

        // Validasi sisa cuti (gunakan elemen pertama sebagai contoh)
        if (annual.getSisaCuti().isEmpty() || annual.getSisaCuti().get(0) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient leave balance");
        }

        // Buat entitas LeaveRequest baru
        LeaveRequest leave = new LeaveRequest();
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setAlasan(request.getAlasan());
        leave.setLeaveAnnual(annual);
        leave.setEmployee(employee);

        // Tambahkan leave request ke dalam daftar karyawan
        employee.getLeaveRequests().add(leave);
        employeeRepository.save(employee);

        // Kurangi sisa cuti (gunakan elemen pertama sebagai contoh)
        List<Long> updatedSisaCuti = annual.getSisaCuti();
        if (updatedSisaCuti == null || updatedSisaCuti.isEmpty() || updatedSisaCuti.get(0) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak cukup sisa cuti");
        }
        updatedSisaCuti.set(0, updatedSisaCuti.get(0) - 1);
        annual.setSisaCuti(updatedSisaCuti);
        employeeAnnualRepository.save(annual);

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

        List<String> namaCuti = leave.getLeaveAnnual().getCompanyLeave().stream()
                .map(CompanyLeave::getJenisCuti)
                .collect(Collectors.toList());

        List<LeaveApprovalRes> leaveApprovalResList = leave.getApprovers().stream()
                .map(leaveApproval -> LeaveApprovalRes.builder()
                        .id(leaveApproval.getId())
                        .idRequest(leaveApproval.getId())
                        .namaApproval(leaveApproval.getApprover().getName())
                        .status(leaveApproval.getStatus())
                        .comment(leaveApproval.getComment())
                        .build())
                .toList();

        return LeaveRequestRes.builder()
                .id(leave.getId())
                .nama(leave.getEmployee().getName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .alasan(leave.getAlasan())
                .status(leave.getStatus())
                .jenis(namaCuti)
                .leaveApprovalRes(leaveApprovalResList)
                .build();
    }

}
