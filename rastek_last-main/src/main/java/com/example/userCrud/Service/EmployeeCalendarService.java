package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.EmployeeCalendarRepository;
import com.example.userCrud.Repository.EmployeeEventRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeCalendarService {
    @Autowired
    ValidationService validationService;

    @Autowired
    EmployeeCalendarRepository employeeCalendarRepository;

    @Autowired
    EmployeeEventRepository employeeEventRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeCalendarRes create(Long nik, EmployeeCalendarReq request) {

        validationService.validate(request);

        EmployeeEvent event = employeeEventRepository.findFirstById(request.getIdEmployeeEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        EmployeeEntity employee = employeeRepository.findById(nik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        EmployeeCalendar calendar = new EmployeeCalendar();

        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendar.setDescription(request.getDescription());
        calendar.setEmployeeEvent(event);
        calendar.setEmployee(employee);

//        event.getCalendars().add(calendar);
//        companyEventRepository.save(event);
        employeeCalendarRepository.save(calendar);

        return toEmployeeCalendarResponse(calendar);
    }

    private EmployeeCalendarRes toEmployeeCalendarResponse(EmployeeCalendar employeeCalendar) {
        return EmployeeCalendarRes.builder()
                .idEmployeeCalendar(employeeCalendar.getId())
                .idEmployeeEvent(employeeCalendar.getEmployeeEvent().getId())
                .nameEvent(employeeCalendar.getEmployeeEvent().getEventName())
                .startDate(employeeCalendar.getStartDate())
                .endDate(employeeCalendar.getEndDate())
                .isCuti(employeeCalendar.getEmployeeEvent().getIsCuti())
                .description(employeeCalendar.getDescription())
                .NIK(employeeCalendar.getEmployee().getNIK())
                .build();
    }

    public List<EmployeeCalendarRes> getAllEmployeeCalendars() {
        List<EmployeeCalendar> calendars = employeeCalendarRepository.findAll();

        return calendars.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EmployeeCalendarRes mapToResponse(EmployeeCalendar calendar) {
        return EmployeeCalendarRes.builder()
                .idEmployeeCalendar(calendar.getId())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .description(calendar.getDescription())
                .idEmployeeEvent(calendar.getEmployeeEvent().getId())
                .nameEvent(calendar.getEmployeeEvent() != null ?
                        calendar.getEmployeeEvent().getEventName() : null)
                .isCuti(calendar.getEmployeeEvent() != null ?
                        calendar.getEmployeeEvent().getIsCuti() : null)
                .NIK(calendar.getEmployee().getNIK())
                .build();
    }

    @Transactional
    public EmployeeCalendarRes updateEmployeeCalendarEvent(Long employeeCalendarId, EmployeeCalendarEventReq updateRequest) {
        validationService.validate(updateRequest);
        EmployeeCalendarReq reqCalendar = updateRequest.getEmployeeCalendarReq();
        EmployeeEventReq reqEvent = updateRequest.getEmployeeEventReq();

        EmployeeEvent event = employeeEventRepository.findById(reqCalendar.getIdEmployeeEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee's Event not found"));

        EmployeeCalendar calendar = employeeCalendarRepository.findById(employeeCalendarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee's Schedule not found"));

        if (Objects.nonNull(reqCalendar.getStartDate())) {
            calendar.setStartDate(reqCalendar.getStartDate());
            employeeCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqCalendar.getEndDate())) {
            calendar.setEndDate(reqCalendar.getEndDate());
            employeeCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqCalendar.getDescription()) && !reqCalendar.getDescription().isEmpty()) {
            calendar.setDescription(reqCalendar.getDescription());
            employeeCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqEvent.getEventName()) && !reqEvent.getEventName().isEmpty()) {
            event.setEventName(reqEvent.getEventName());
            employeeEventRepository.save(event);
        }
        if (Objects.nonNull(reqEvent.getIsCuti())) {
            event.setIsCuti(reqEvent.getIsCuti());
            employeeEventRepository.save(event);
        }

        employeeEventRepository.save(event);
        employeeCalendarRepository.save(calendar);
        return mapToResponse(calendar);
    }

    @Transactional
    public void deleteCalendar(Long calendarId) {
        EmployeeCalendar calendar = employeeCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        employeeCalendarRepository.delete(calendar);
    }

    @Transactional
    public EmployeeCalendarRes getCalendarByNIK(Long nik) {
        EmployeeCalendar calendar = employeeCalendarRepository.findByEmployeeNIK(nik)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee's Schedule not found"));

        return toEmployeeCalendarResponse(calendar);
    }
}
