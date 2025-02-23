package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.JabatanEntity;
import com.example.userCrud.Repository.CompanyLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyLeaveService {

    @Autowired
    ValidationService validationService;

    @Autowired
    CompanyLeaveRepository companyLeaveRepository;


    @Transactional
    public CompanyLeaveRes create(CompanyLeaveReq request) {
        validationService.validate(request);

        CompanyLeave companyLeave = new CompanyLeave();

        companyLeave.setJenisCuti(request.getJenisCuti());

        companyLeaveRepository.save(companyLeave);

        return toCompanyLeaveRes(companyLeave);
    }

    @Transactional(readOnly = true)
    public List<CompanyLeaveRes> getAllCompanyLeaveWithInfo() {
        List<CompanyLeave> companyLeaves = companyLeaveRepository.findAll();

        return companyLeaves.stream()
                .map(this::toCompanyLeaveRes)
                .collect(Collectors.toList());
    }

    private CompanyLeaveRes toCompanyLeaveRes(CompanyLeave leave){
        return CompanyLeaveRes.builder()
                .id(leave.getId())
                .jenisCuti(leave.getJenisCuti())
                .build();
    }

}
