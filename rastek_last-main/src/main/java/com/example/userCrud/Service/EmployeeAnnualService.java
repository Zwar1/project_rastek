package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Entity.EmployeeAnnual;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Repository.CompanyLeaveRepository;
import com.example.userCrud.Repository.EmployeeAnnualRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeAnnualService {

    @Autowired
    ValidationService validationService;

    @Autowired
    EmployeeAnnualRepository employeeAnnualRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyLeaveRepository companyLeaveRepository;

    @Transactional
    public List<EmployeeAnnualListRes> createMultiple(List<EmployeeAnnualListReq> requests) {
        List<EmployeeAnnualListRes> responses = new ArrayList<>();

        for (EmployeeAnnualListReq request : requests) {
            // Validasi request
            validationService.validate(request);

            // Cari employee berdasarkan ID
            EmployeeEntity employeeEntity = employeeRepository.findById(request.getNik())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

            List<EmployeeAnnualRes> leaveQuotaList = new ArrayList<>();

            for (EmployeeAnnualReq jatahCuti : request.getJatahCuti()) {
                // Ambil jenis cuti berdasarkan ID

                if (jatahCuti.getIdJenisCuti() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jenis Cuti ID Tidak Boleh Null");
                }
                CompanyLeave companyLeave = companyLeaveRepository.findFirstById(jatahCuti.getIdJenisCuti())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Cuti tidak ditemukan"));

                // Buat EmployeeAnnual baru
                EmployeeAnnual annual = new EmployeeAnnual();
                annual.setEmployee(employeeEntity);
                annual.setCompanyLeave(companyLeave);
                annual.setSisaCuti(jatahCuti.getSisaCuti());

                // Simpan entitas
                employeeAnnualRepository.save(annual);

                // Menambahkan response ke list
                leaveQuotaList.add(toEmployeeAnnualResponse(annual));
            }

            // Membuat response untuk seluruh jatah cuti karyawan
            responses.add(toEmployeeAnnualListResponse(employeeEntity, leaveQuotaList));
        }

        return responses;
    }



    @Transactional(readOnly = true)
    public EmployeeAnnualListRes getByEmployeeId(Long employeeId) {
        // Cari employee berdasarkan ID
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        // Ambil semua jatah cuti berdasarkan employeeId
        List<EmployeeAnnual> employeeAnnuals = employeeAnnualRepository.findAllByEmployee(employee);

        if (employeeAnnuals.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Leave Quotas Found for Employee");
        }

        // Konversi Entity (EmployeeAnnual) ke DTO (EmployeeAnnualRes)
        List<EmployeeAnnualRes> leaveQuotaList = employeeAnnuals.stream()
                .map(this::toEmployeeAnnualResponse) // Convert each EmployeeAnnual to EmployeeAnnualRes
                .collect(Collectors.toList());

        // Mengembalikan response yang sesuai dengan format yang dibutuhkan
        return toEmployeeAnnualListResponse(employee, leaveQuotaList);
    }

    @Transactional
    public EmployeeAnnualListRes updateEmployeeAnnualList(long employeeId, EmployeeAnnualListReq updateRequest) {
        validationService.validate(updateRequest);

        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        if (employeeId != updateRequest.getNik()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Employee ID in path doesn't match with employee ID in request body");
        }

        List<EmployeeAnnual> existingLeaves = employeeAnnualRepository.findAllByEmployee(employeeEntity);

        // Map by company leave ID for easier lookup
        Map<Long, EmployeeAnnual> existingLeavesMap = existingLeaves.stream()
                .collect(Collectors.toMap(
                        leave -> leave.getCompanyLeave().getId(),
                        leave -> leave
                ));

        List<EmployeeAnnualRes> updatedLeaveQuotaList = new ArrayList<>();

        for (EmployeeAnnualReq leaveQuotaReq : updateRequest.getJatahCuti()) {
            if (leaveQuotaReq.getIdJenisCuti() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jenis Cuti ID Tidak Boleh Null");
            }

            // Check if the leave type exists
            CompanyLeave companyLeave = companyLeaveRepository.findFirstById(leaveQuotaReq.getIdJenisCuti())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Jenis Cuti dengan ID " + leaveQuotaReq.getIdJenisCuti() + " tidak ditemukan"));

            EmployeeAnnual leaveQuota;

            if (existingLeavesMap.containsKey(leaveQuotaReq.getIdJenisCuti())) {
                leaveQuota = existingLeavesMap.get(leaveQuotaReq.getIdJenisCuti());
                leaveQuota.setSisaCuti(leaveQuotaReq.getSisaCuti());
            } else {
                // Create new leave quota if it doesn't exist
                leaveQuota = new EmployeeAnnual();
                leaveQuota.setEmployee(employeeEntity);
                leaveQuota.setCompanyLeave(companyLeave);
                leaveQuota.setSisaCuti(leaveQuotaReq.getSisaCuti());
            }

            employeeAnnualRepository.save(leaveQuota);

            updatedLeaveQuotaList.add(toEmployeeAnnualResponse(leaveQuota));
        }

        return toEmployeeAnnualListResponse(employeeEntity, updatedLeaveQuotaList);
    }

    private EmployeeAnnualListRes toEmployeeAnnualListResponse(EmployeeEntity employee, List<EmployeeAnnualRes> leaveQuotaList) {
        return EmployeeAnnualListRes.builder()
                .namaEmployee(employee.getName())
                .jatahCuti(leaveQuotaList) // leaveQuotas di sini adalah List<EmployeeAnnualRes>
                .build();
    }

    private EmployeeAnnualRes toEmployeeAnnualResponse(EmployeeAnnual annual) {
        return EmployeeAnnualRes.builder()
                .id(annual.getCompanyLeave().getId())
                .jenisCuti(annual.getCompanyLeave().getJenisCuti())
                .sisaCuti(annual.getSisaCuti())
                .build();
    }

//    private EmployeeAnnualListRes toEmployeeAnnualResponse(EmployeeEntity employee, List<EmployeeAnnual> employeeAnnuals) {
//        List<EmployeeAnnualRes> leaveQuotas = employeeAnnuals.stream()
//                .map(annual -> EmployeeAnnualRes.builder()
//                        .jenisCuti(annual.getCompanyLeave().getJenisCuti())
//                        .sisaCuti(annual.getSisaCuti())
//                        .build())
//                .collect(Collectors.toList());
//
//        return EmployeeAnnualListRes.builder()
//                .namaEmployee(employee.getName())
//                .jatahCuti(leaveQuotas)
//                .build();
//    }


}
