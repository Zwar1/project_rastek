package com.example.userCrud.Service;

import com.example.userCrud.Dto.CompanyLeaveRes;
import com.example.userCrud.Dto.EmployeeAnnualReq;
import com.example.userCrud.Dto.EmployeeAnnualRes;
import com.example.userCrud.Entity.CompanyLeave;
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

import java.util.List;
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
    public EmployeeAnnualRes create(EmployeeAnnualReq request){
        validationService.validate(request);

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getEmployee())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));


        List<CompanyLeave> companyLeave = companyLeaveRepository.findAllById(request.getIdJenisCuti());

        if (companyLeave.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jenis Cuti Not Found");
        }

        EmployeeAnnual annual = new EmployeeAnnual();

        annual.setEmployee(employeeEntity);
        annual.setCompanyLeave(companyLeave);
        annual.setSisaCuti(request.getSisaCuti());

        employeeEntity.getAnnuals().add(annual);

        employeeRepository.save(employeeEntity);

        employeeAnnualRepository.save(annual);

        return toEmployeeAnnualResponse(annual);

    }


    @Transactional(readOnly = true)
    public EmployeeAnnualRes get(Long id) {
        EmployeeAnnual annual = employeeAnnualRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Annual Not Found"));

        return toEmployeeAnnualResponse(annual);
    }

    private EmployeeAnnualRes toEmployeeAnnualResponse(EmployeeAnnual annual) {
        List<CompanyLeaveRes> companyLeaveResponses = annual.getCompanyLeave().stream()
                .map(leave -> {
                    CompanyLeaveRes response = new CompanyLeaveRes();
                    response.setId(leave.getId());
                    response.setJenisCuti(leave.getJenisCuti());
                    return response;
                })
                .collect(Collectors.toList());

        return EmployeeAnnualRes.builder()
                .id(annual.getId())
                .jenisCuti(companyLeaveResponses)
                .namaEmployee(annual.getEmployee().getName())
                .sisaCuti(annual.getSisaCuti())
                .build();
    }


}
