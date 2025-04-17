package com.example.userCrud.Service;


import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Entity.DivisionEntity;
import com.example.userCrud.Repository.DepartementRepository;
import com.example.userCrud.Repository.DivisionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    DepartementRepository departementRepository;

    @Autowired
    ValidationService validationService;

    @Transactional
    public DivisionRes create(DivisionReq request) {
        validationService.validate(request);

        DepartementEntity departement = departementRepository.findFirstById(request.getDepartementId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement not found"));

        DivisionEntity division = new DivisionEntity();

        division.setDivision_name(request.getDivision_name());

        divisionRepository.save(division);

        if (departement.getDivisionEntities() == null) {
            departement.setDivisionEntities(new HashSet<>());
        }
        departement.getDivisionEntities().add(division);

        departementRepository.save(departement);

        return DivisionRes.builder()
                .id(division.getId())
                .division_name(division.getDivision_name())
                .build();
    }

    @Transactional(readOnly = true)
    public DivisionRes get(Long id){
        DivisionEntity division = divisionRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Division Not Found"));

        return DivisionRes.builder()
                .id(division.getId())
                .division_name(division.getDivision_name())
                .build();
    }

    @Transactional(readOnly = true)
    public List<DivisionRes> getAllDivision(){
        List<DivisionEntity> divisionEntities = divisionRepository.findAll();

        return divisionEntities.stream().map(
                division -> DivisionRes.builder()
                        .id(division.getId())
                        .division_name(division.getDivision_name())
                        .build()).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        DivisionEntity division = divisionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Division Not Found"));

        divisionRepository.delete(division);
    }

    @Transactional
    public DivisionRes update(UpdateDivisionReq request) {
        validationService.validate(request);

        // First, attempt to find the entity. If it's not found, it will throw.
        DivisionEntity division = divisionRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Division Not Found"));

        // Update fields
        division.setDivision_name(request.getDivision_name());

        // Merge and save the updated entity
        divisionRepository.save(division); // This might be redundant if you are using merge correctly.

        return DivisionRes.builder()
                .id(division.getId())
                .division_name(division.getDivision_name())
                .build();
    }

}
