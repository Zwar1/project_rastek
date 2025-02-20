package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Repository.CompanyLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        companyLeave.setJenisCuti(request.getNamaCuti());
        companyLeave.setJatahawal(companyLeave.getJatahawal());

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

    @Transactional(readOnly = true)
    public CompanyLeaveRes get(Long id){
        CompanyLeave companyLeave = companyLeaveRepository.findFirstById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Cuti Not Found"));

        return toCompanyLeaveRes(companyLeave);
    }

    private CompanyLeaveRes toCompanyLeaveRes(CompanyLeave leave){
        return CompanyLeaveRes.builder()
                .id(leave.getId())
                .namaCuti(leave.getJenisCuti())
                .build();
    }

}
