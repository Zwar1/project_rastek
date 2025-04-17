package com.example.userCrud.Service;

import com.example.userCrud.Dto.CompanyCalendarEventReq;
import com.example.userCrud.Dto.CompanyCalendarReq;
import com.example.userCrud.Dto.CompanyCalendarRes;
import com.example.userCrud.Dto.CompanyEventReq;
import com.example.userCrud.Entity.CompanyCalendar;
import com.example.userCrud.Entity.CompanyEvent;
import com.example.userCrud.Repository.CompanyCalendarRepository;
import com.example.userCrud.Repository.CompanyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class CompanyCalendarService {

    @Autowired
    ValidationService validationService;

    @Autowired
    CompanyCalendarRepository companyCalendarRepository;

    @Autowired
    CompanyEventRepository companyEventRepository;

    @Transactional
    public CompanyCalendarRes create(CompanyCalendarReq request) {

        validationService.validate(request);

        CompanyEvent event = companyEventRepository.findFirstById(request.getIdEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        CompanyCalendar calendar = new CompanyCalendar();

        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendar.setDescription(request.getDescription());
        calendar.setCompanyEvent(event);

//        event.getCalendars().add(calendar);
//        companyEventRepository.save(event);
        companyCalendarRepository.save(calendar);

        return toCompanyCalendarResponse(calendar);
    }

    private CompanyCalendarRes toCompanyCalendarResponse(CompanyCalendar companyCalendar) {
        return CompanyCalendarRes.builder()
                .idCalendar(companyCalendar.getId())
                .idEvent(companyCalendar.getCompanyEvent().getId())
                .nameEvent(companyCalendar.getCompanyEvent().getEventName())
                .startDate(companyCalendar.getStartDate())
                .endDate(companyCalendar.getEndDate())
                .isFree(companyCalendar.getCompanyEvent().getIsFree())
                .description(companyCalendar.getDescription())
                .build();
    }

    public List<CompanyCalendarRes> getAllCalendars() {
        List<CompanyCalendar> calendars = companyCalendarRepository.findAll();

        return calendars.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CompanyCalendarRes mapToResponse(CompanyCalendar calendar) {
        return CompanyCalendarRes.builder()
                .idCalendar(calendar.getId())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .description(calendar.getDescription())
                .idEvent(calendar.getCompanyEvent().getId())
                .nameEvent(calendar.getCompanyEvent() != null ?
                        calendar.getCompanyEvent().getEventName() : null)
                .isFree(calendar.getCompanyEvent() != null ?
                        calendar.getCompanyEvent().getIsFree() : null)
                .build();
    }

    @Transactional
    public CompanyCalendarRes updateCalendarEvent(Long calendarId, CompanyCalendarEventReq updateRequest) {
        validationService.validate(updateRequest);
        CompanyCalendarReq reqCalendar = updateRequest.getCalendarReq();
        CompanyEventReq reqEvent = updateRequest.getEventReq();

        CompanyEvent event = companyEventRepository.findById(reqCalendar.getIdEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        CompanyCalendar calendar = companyCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        if (Objects.nonNull(reqCalendar.getStartDate())) {
            calendar.setStartDate(reqCalendar.getStartDate());
            companyCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqCalendar.getEndDate())) {
            calendar.setEndDate(reqCalendar.getEndDate());
            companyCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqCalendar.getDescription()) && !reqCalendar.getDescription().isEmpty()) {
            calendar.setDescription(reqCalendar.getDescription());
            companyCalendarRepository.save(calendar);
        }
        if (Objects.nonNull(reqEvent.getEventName()) && !reqEvent.getEventName().isEmpty()) {
            event.setEventName(reqEvent.getEventName());
            companyEventRepository.save(event);
        }
        if (Objects.nonNull(reqEvent.getIsFree())) {
            event.setIsFree(reqEvent.getIsFree());
            companyEventRepository.save(event);
        }

        companyEventRepository.save(event);
        companyCalendarRepository.save(calendar);
        return mapToResponse(calendar);
    }

    @Transactional
    public void deleteCalendar(Long calendarId) {
        CompanyCalendar calendar = companyCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        companyCalendarRepository.delete(calendar);
    }
}
