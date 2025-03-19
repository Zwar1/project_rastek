package com.example.userCrud.Service;

import com.example.userCrud.Dto.CompanyEventReq;
import com.example.userCrud.Dto.CompanyEventRes;
import com.example.userCrud.Entity.CompanyEvent;
import com.example.userCrud.Repository.CompanyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompanyEventService {

    @Autowired
    ValidationService validationService;

    @Autowired
    CompanyEventRepository companyEventRepository;

    @Transactional
    public CompanyEventRes create(CompanyEventReq request) {
        validationService.validate(request);

        CompanyEvent companyEvent = new CompanyEvent();

        companyEvent.setEventName(request.getEventName());
        companyEvent.setIsFree(request.getIsFree());

        companyEventRepository.save(companyEvent);

        return CompanyEventRes.builder()
                .id(companyEvent.getId())
                .eventName(companyEvent.getEventName())
                .isFree(companyEvent.getIsFree())
                .build();
    }

    @Transactional
    public CompanyEventRes get(Long id) {
        CompanyEvent event = companyEventRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Event Not Found"));

        return CompanyEventRes.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .isFree(event.getIsFree())
                .build();
    }

    @Transactional
    public List<CompanyEventRes> getAllCompanyEvent() {
        List<CompanyEvent> events = companyEventRepository.findAll();

        return events.stream().map(
                allCompanyEvent -> CompanyEventRes.builder()
                        .id(allCompanyEvent.getId())
                        .eventName(allCompanyEvent.getEventName())
                        .isFree(allCompanyEvent.getIsFree())
                        .build()).collect(Collectors.toList());
    }

    @Transactional
    public CompanyEventRes updateEvent(Long id, CompanyEventReq req) {
        validationService.validate(req);

        CompanyEvent event = companyEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (Objects.nonNull(req.getEventName()) && !req.getEventName().isEmpty()) {
            event.setEventName(req.getEventName());
            companyEventRepository.save(event);
        }
        event.setIsFree(req.getIsFree());

        companyEventRepository.save(event);
        return CompanyEventRes.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .isFree(event.getIsFree())
                .build();
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        CompanyEvent event = companyEventRepository.findFirstById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        companyEventRepository.delete(event);
    }
}
