package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JabatanService {

    @Autowired
    private RiwayatJabatanRepository riwayatJabatanRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public JabatanRes create(JabatanReq request) {
        validationService.validate(request);

        JabatanEntity jabatan = new JabatanEntity();

        jabatan.setId(jabatan.getId());
        jabatan.setKodeJabatan(request.getKodeJabatan());
        jabatan.setNamaJabatan(request.getNamaJabatan());
        jabatan.setSequence(request.getSequence());
        jabatan.setAtasan(request.getIsAtasan());

        // Set DepartementEntity
        if (request.getDepartementId() != null) {
            DepartementEntity departementEntity = departementRepository.findFirstById(request.getDepartementId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement Not Found"));
            jabatan.setDepartementEntity(departementEntity);
        }

        // Set DivisionEntity
        if (request.getDivisionId() != null) {
            DivisionEntity divisionEntity = divisionRepository.findById(request.getDivisionId())
                    .orElse(null);
            if (divisionEntity != null) {
                jabatan.setDivisionEntity(divisionEntity);
            }
        }

        jabatanRepository.save(jabatan);

        return toJabatanResponse(jabatan);

    }

    @Transactional(readOnly = true)
    public JabatanRes get(Long id) {
        JabatanEntity jabatan = jabatanRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));

        if (jabatan != null) {
            return toJabatanResponse(jabatan);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found");
        }
    }


    @Transactional
    public JabatanRes update(UpdateJabatanReq request) {
        validationService.validate(request);


        JabatanEntity jabatan = jabatanRepository.findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));
        if (jabatan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found");
        }


        if (Objects.nonNull(request.getKodeJabatan()) && !request.getKodeJabatan().isEmpty()) {
            jabatan.setKodeJabatan(request.getKodeJabatan());

            jabatanRepository.save(jabatan);
        }

        if (Objects.nonNull(request.getNamaJabatan()) && !request.getNamaJabatan().isEmpty()) {
            jabatan.setNamaJabatan(request.getNamaJabatan());

            jabatanRepository.save(jabatan);
        }

        if (Objects.nonNull(request.getSequence()) && request.getSequence() > 0) {
            jabatan.setSequence(request.getSequence());
            jabatanRepository.save(jabatan);
        }

        if (Objects.nonNull(request.getIsAtasan()) && !request.getIsAtasan().describeConstable().isEmpty()) {
            jabatan.setAtasan(request.getIsAtasan());

            jabatanRepository.save(jabatan);
        }

        // Set DepartementEntity
        if (request.getDepartementId() != null) {
            DepartementEntity departementEntity = departementRepository.findById(request.getDepartementId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departement Not Found"));
            jabatan.setDepartementEntity(departementEntity);

            jabatanRepository.save(jabatan);
        }

        // Set DivisionEntity
        if (request.getDivisionId() != null) {
            DivisionEntity divisionEntity = divisionRepository.findById(request.getDivisionId())
                    .orElseThrow(() -> new EntityNotFoundException("Division not found"));
            jabatan.setDivisionEntity(divisionEntity);

            jabatanRepository.save(jabatan);
        }

        return toJabatanResponse(jabatan);
    }

    @Transactional
    public void delete(Long id){
        JabatanEntity jabatan = jabatanRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));

        if (jabatan != null) {
            jabatanRepository.delete(jabatan);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found");
        }
    }

    @Transactional(readOnly = true)
    public List<JabatanRes> getAllJabatan(){
        List<JabatanEntity> jabatanEntities = jabatanRepository.findAll();

        return jabatanEntities.stream().map(
                jabatan -> JabatanRes.builder()
                        .id(jabatan.getId())
                        .namaJabatan(jabatan.getNamaJabatan())
                        .kodeJabatan(jabatan.getKodeJabatan())
                        .isAtasan(jabatan.isAtasan())
                        .sequence(jabatan.getSequence())
                        .departement(jabatan.getDepartementEntity().getDepartement_name())
                        .id_division(jabatan.getDivisionEntity() != null ? jabatan.getDivisionEntity().getId() : null)
                        .division(jabatan.getDivisionEntity() != null ? jabatan.getDivisionEntity().getDivision_name() : null)
                        .build()).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<String> getAllKodeJabatan() {
        return jabatanRepository.findAll().stream()
                .map(JabatanEntity::getKodeJabatan)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllNamaJabatan() {
        return jabatanRepository.findAll().stream()
                .map(JabatanEntity::getNamaJabatan)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getNamaJabatanAtasanInSameDepartement(Long NIK) {
        // Ambil EmployeeEntity berdasarkan NIK
        EmployeeEntity employee = employeeRepository.findFirstByNIK(NIK)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // Ambil daftar riwayat jabatan dari EmployeeEntity
        List<RiwayatJabatanEntity> riwayatJabatanList = employee.getRiwayatJabatan();

        // Pastikan employee memiliki riwayat jabatan
        if (riwayatJabatanList == null || riwayatJabatanList.isEmpty()) {
            throw new EntityNotFoundException("No job history found for the employee");
        }

        // Ambil DepartementEntity dari riwayat jabatan terbaru (misalnya index 0)
        DepartementEntity departementEntity = riwayatJabatanList.get(0).getId_jabatan().getDepartementEntity();
        if (departementEntity == null) {
            throw new EntityNotFoundException("Departement not found for the employee");
        }

        // Cari namaJabatan di departemen tersebut dengan isAtasan = true
        return jabatanRepository.findNamaJabatanByDepartementAndIsAtasan(departementEntity.getId(), true);
    }


    private JabatanRes toJabatanResponse(JabatanEntity jabatan) {

        return JabatanRes.builder()
                .id(jabatan.getId())
                .kodeJabatan(jabatan.getKodeJabatan())
                .namaJabatan(jabatan.getNamaJabatan())
                .isAtasan(jabatan.isAtasan())
                .sequence(jabatan.getSequence())
                .departement(jabatan.getDepartementEntity().getDepartement_name())
                .division(jabatan.getDivisionEntity() != null ? jabatan.getDivisionEntity().getDivision_name() : null)
                .build();
    }

}