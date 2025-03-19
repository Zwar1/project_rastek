package com.example.userCrud.Service;

import com.example.userCrud.Dto.CompanyEventReq;
import com.example.userCrud.Dto.CompanyEventRes;
import com.example.userCrud.Dto.EmployeeEventReq;
import com.example.userCrud.Dto.EmployeeEventRes;
import com.example.userCrud.Entity.CompanyEvent;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.EmployeeEvent;
import com.example.userCrud.Repository.EmployeeEventRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeEventService {
    @Autowired
    ValidationService validationService;

    @Autowired
    EmployeeEventRepository employeeEventRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeEventRes create(Long nik, EmployeeEventReq request) {
        validationService.validate(request);

        EmployeeEntity employee = employeeRepository.findById(nik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        EmployeeEvent employeeEvent = new EmployeeEvent();

        employeeEvent.setEventName(request.getEventName());
        employeeEvent.setIsCuti(request.getIsCuti());
        employeeEvent.setEmployee(employee);

        employeeEventRepository.save(employeeEvent);

        return EmployeeEventRes.builder()
                .id(employeeEvent.getId())
                .eventName(employeeEvent.getEventName())
                .isCuti(employeeEvent.getIsCuti())
                .NIK(employeeEvent.getEmployee().getNIK())
                .build();
    }

    @Transactional
    public EmployeeEventRes get(Long id) {
        EmployeeEvent event = employeeEventRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Event Not Found"));

        return EmployeeEventRes.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .isCuti(event.getIsCuti())
                .NIK(event.getEmployee().getNIK())
                .build();
    }

    @Transactional
    public List<EmployeeEventRes> getAllEmployeeEvent() {
        List<EmployeeEvent> events = employeeEventRepository.findAll();

        return events.stream().map(
                allEmployeeEvent -> EmployeeEventRes.builder()
                        .id(allEmployeeEvent.getId())
                        .eventName(allEmployeeEvent.getEventName())
                        .isCuti(allEmployeeEvent.getIsCuti())
                        .NIK(allEmployeeEvent.getEmployee().getNIK())
                        .build()).collect(Collectors.toList());
    }

    @Transactional
    public EmployeeEventRes updateEvent(Long id, EmployeeEventReq req) {
        validationService.validate(req);

        EmployeeEvent event = employeeEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EMployee's Event not found"));

        // Update employee if NIK changed
//        if (Objects.nonNull(req.getNIK()) && !req.getNIK().equals(event.getEmployee().getNIK())) {
//            EmployeeEntity newEmployee = employeeRepository.findById(req.getNIK())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "New Employee not found"));
//            event.setEmployee(newEmployee);
//        }
        if (Objects.nonNull(req.getEventName()) && !req.getEventName().isEmpty()) {
            event.setEventName(req.getEventName());
            employeeEventRepository.save(event);
        }
        event.setIsCuti(req.getIsCuti());

        employeeEventRepository.save(event);
        return EmployeeEventRes.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .isCuti(event.getIsCuti())
                .NIK(event.getEmployee().getNIK())
                .build();
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        EmployeeEvent event = employeeEventRepository.findFirstById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        employeeEventRepository.delete(event);
    }
}
