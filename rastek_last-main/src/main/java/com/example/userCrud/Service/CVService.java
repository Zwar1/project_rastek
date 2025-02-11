package com.example.userCrud.Service;

import com.example.userCrud.Dto.CVReq;
import com.example.userCrud.Dto.CVRes;
import com.example.userCrud.Dto.EmployeeCVRes;
import com.example.userCrud.Entity.CVEntity;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Repository.CVRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CVService {
    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CVRepository cvRepository;

    @Transactional
    public CVRes addCV(Long nik, CVReq request) {
        validationService.validate(request);

        EmployeeEntity employee = employeeRepository.findFirstByNIK(nik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        CVEntity cvEntity = new CVEntity();
        cvEntity.setProjectName(request.getProjectName());
        cvEntity.setProjectRole(request.getProjectRole());
        cvEntity.setProjectStart(request.getProjectStart());
        cvEntity.setProjectEnd(request.getProjectEnd());
        cvEntity.setProjectDescription(request.getProjectDescription());
        cvEntity.setEmployee(employee);

        cvEntity = cvRepository.save(cvEntity); // Save the new CV entry

        return CVRes.builder()
                .id(cvEntity.getId())
                .projectName(cvEntity.getProjectName())
                .projectRole(cvEntity.getProjectRole())
                .projectStart(cvEntity.getProjectStart())
                .projectEnd(cvEntity.getProjectEnd())
                .projectDescription(cvEntity.getProjectDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public EmployeeCVRes getEmployeeCv(Long nik) {
        EmployeeEntity employee = employeeRepository.findFirstByNIK(nik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        List<CVRes> cvList = employee.getCv().stream().map(cv -> CVRes.builder()
                .id(cv.getId())
                .projectName(cv.getProjectName())
                .projectRole(cv.getProjectRole())
                .projectStart(cv.getProjectStart())
                .projectEnd(cv.getProjectEnd())
                .projectDescription(cv.getProjectDescription())
                .build()).toList();

        return EmployeeCVRes.builder()
                .nama(employee.getName())
                .nik(employee.getNIK())
                .cv(cvList)
                .build();
    }

    @Transactional
    public void deleteCV(@PathVariable("id") Long id) {
        CVEntity cvEntity = cvRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV Not Found"));

        cvRepository.delete(cvEntity);
    }
}
