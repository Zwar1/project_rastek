package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Repository.DepartementRepository;
import com.example.userCrud.Repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    @Autowired
    DepartementRepository departementRepository;

    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    ValidationService validationService;

    @Transactional
    public DepartementRes create(DepartementReq request) {
        validationService.validate(request);

        DepartementEntity departement = new DepartementEntity();

        departement.setDepartement_name(request.getDepartement_name());
        departement.setDepartement_head(request.getDepartement_head());

        departementRepository.save(departement);

        return DepartementRes.builder()
                .id(departement.getId())
                .departement_name(departement.getDepartement_name())
                .departement_head(departement.getDepartement_head())
                .build();
    }

    @Transactional(readOnly = true)
    public DepartementRes get(Long id){
        DepartementEntity departement = departementRepository.findFirstById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement Not Found"));

        return DepartementRes.builder()
                .id(departement.getId())
                .departement_name(departement.getDepartement_name())
                .departement_head(departement.getDepartement_head())
                .build();
    }

    @Transactional(readOnly = true)
    public List<DepartementRes> getAllDepartement(){
        List<DepartementEntity> departementEntities = departementRepository.findAll();

        return departementEntities.stream().map(
            departement -> DepartementRes.builder()
                    .id(departement.getId())
                    .departement_name(departement.getDepartement_name())
                    .departement_head(departement.getDepartement_head())
                    .build()).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        DepartementEntity departement = departementRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement Not Found"));

        departementRepository.delete(departement);
    }

    @Transactional
    public  DepartementRes update(UpdateDepartementReq request) {

        validationService.validate(request);


        DepartementEntity departement = departementRepository.findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement Not Found"));

        if (Objects.nonNull(request.getDepartement_head()) && !request.getDepartement_head().isEmpty()) {
            departement.setDepartement_head(request.getDepartement_head());
            departementRepository.save(departement);
        }
        if (Objects.nonNull(request.getDepartement_name()) && !request.getDepartement_name().isEmpty()) {
            departement.setDepartement_name(request.getDepartement_name());
            departementRepository.save(departement);
        }


        return DepartementRes.builder()
                .id(departement.getId())
                .departement_name(departement.getDepartement_name())
                .departement_head(departement.getDepartement_head())
                .build();
    }
}
