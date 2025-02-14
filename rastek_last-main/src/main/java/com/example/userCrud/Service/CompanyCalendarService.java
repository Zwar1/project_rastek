package com.example.userCrud.Service;

import com.example.userCrud.Dto.CompanyCalendarReq;
import com.example.userCrud.Dto.CompanyCalendarRes;
import com.example.userCrud.Dto.CompanyEventReq;
import com.example.userCrud.Dto.CompanyEventRes;
import com.example.userCrud.Entity.CompanyCalendar;
import com.example.userCrud.Entity.CompanyEvent;
import com.example.userCrud.Repository.CompanyCalendarRepository;
import com.example.userCrud.Repository.CompanyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public CompanyCalendarRes create(CompanyCalendarReq request){

        validationService.validate(request);

        CompanyEvent event = companyEventRepository.findFirstById(request.getIdEvent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        CompanyCalendar calendar = new CompanyCalendar();

        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendar.setDescription(request.getDescription());
        calendar.setCompanyEvent(event);

        event.getCalendars().add(calendar);
        companyEventRepository.save(event);
        companyCalendarRepository.save(calendar);

        return toCompanyCalendarResponse(calendar);
    }

    private CompanyCalendarRes toCompanyCalendarResponse(CompanyCalendar companyCalendar){
        return CompanyCalendarRes.builder()
                .idCalendar(companyCalendar.getId())
                .nameEvent(companyCalendar.getCompanyEvent().getEventName())
                .startDate(companyCalendar.getStartDate())
                .endDate(companyCalendar.getEndDate())
                .isFree(companyCalendar.getCompanyEvent().getIsFree())
                .description(companyCalendar.getDescription())
                .build();
    }

}
