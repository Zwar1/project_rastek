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
import java.util.stream.Collectors;

@Service
public class CompanyEventService {

    @Autowired
    ValidationService validationService;

    @Autowired
    CompanyEventRepository companyEventRepository;

    @Transactional
    public CompanyEventRes create(CompanyEventReq request){
        validationService.validate(request);

        CompanyEvent companyEvent = new CompanyEvent();

        companyEvent.setEventName(request.getEventName());

        companyEventRepository.save(companyEvent);

        return CompanyEventRes.builder()
                .id(companyEvent.getId())
                .eventName(companyEvent.getEventName())
                .build();
    }

    @Transactional
    public CompanyEventRes get(Long id){
        CompanyEvent event = companyEventRepository.findFirstById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Event Not Found"));

        return CompanyEventRes.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .build();
    }

    @Transactional
    public List<CompanyEventRes> getAllCompanyEvent(){
        List<CompanyEvent> events = companyEventRepository.findAll();

        return events.stream().map(
                allCompanyEvent -> CompanyEventRes.builder()
                        .id(allCompanyEvent.getId())
                        .eventName(allCompanyEvent.getEventName())
                        .build()).collect(Collectors.toList());
    }

}
