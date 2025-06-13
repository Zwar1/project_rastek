package com.example.userCrud.Service;


import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class RiwayatJabatanService {
    @Autowired
    private RiwayatJabatanRepository riwayatJabatanRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public RiwayatJabatanRes create(RiwayatJabatanReq request){
        validationService.validate(request);

        RiwayatJabatanEntity riwayatJabatan = new RiwayatJabatanEntity();

        riwayatJabatan.setStatusKontrak(request.getStatusKontrak());
        riwayatJabatan.setTmt_mulai(request.getTmt_awal());
        riwayatJabatan.setTmt_akhir(request.getTmt_akhir());
        riwayatJabatan.setKontrakKedua(request.getKontrakKedua());
        riwayatJabatan.setSalary(request.getSalary());


        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        if (request.getIdJabatan() != null) {
            JabatanEntity jabatanEntityS = jabatanRepository.findFirstById(request.getIdJabatan())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));
            riwayatJabatan.setId_jabatan(jabatanEntityS);
        }

        employeeEntity.getRiwayatJabatan().add(riwayatJabatan);

        riwayatJabatanRepository.save(riwayatJabatan);

        return toRiwayatResponse(riwayatJabatan);
    }

    @Transactional(readOnly = true)
    public RiwayatJabatanRes get(Long id) {
        RiwayatJabatanEntity riwayatJabatan = riwayatJabatanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riwayat Jabatan Not Found"));
        return toRiwayatResponse(riwayatJabatan);
    }

    @Transactional(readOnly = true)
    public List<RiwayatJabatanRes> getAll() {
        List<RiwayatJabatanEntity> riwayatJabatan = riwayatJabatanRepository.findAll();
        return riwayatJabatan.stream().map(this::toRiwayatResponse).collect(Collectors.toList());
    }

    @Transactional
    public RiwayatJabatanRes update(UpdateRiwayatJabatanReq request) {
        validationService.validate(request);

        RiwayatJabatanEntity riwayatJabatan = riwayatJabatanRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riwayat Jabatan Not Found"));

        if (Objects.nonNull(request.getStatusKontrak()) && !request.getStatusKontrak().isEmpty()) {
            riwayatJabatan.setStatusKontrak(request.getStatusKontrak());

            riwayatJabatanRepository.save(riwayatJabatan);
        }
        if (request.getTmt_awal() != null) {
            riwayatJabatan.setTmt_mulai(request.getTmt_awal());

            riwayatJabatanRepository.save(riwayatJabatan);
        }
        if (request.getTmt_akhir() != null) {
            riwayatJabatan.setTmt_akhir(request.getTmt_akhir());

            riwayatJabatanRepository.save(riwayatJabatan);
        }
        if (Objects.nonNull(request.getKontrakKedua()) && !request.getKontrakKedua().isEmpty()) {
            riwayatJabatan.setKontrakKedua(request.getKontrakKedua());

            riwayatJabatanRepository.save(riwayatJabatan);
        }
        if (request.getSalary() != null) {
            riwayatJabatan.setSalary(request.getSalary());

            riwayatJabatanRepository.save(riwayatJabatan);
        }

        if (request.getIdJabatan() != null) {
            JabatanEntity jabatanEntityS = jabatanRepository.findFirstById(request.getIdJabatan())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));
            riwayatJabatan.setId_jabatan(jabatanEntityS);

            riwayatJabatanRepository.save(riwayatJabatan);
        }
        return toRiwayatResponse(riwayatJabatan);
    }

    @Transactional
    public void delete(Long id){
        RiwayatJabatanEntity riwayatJabatan = riwayatJabatanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riwayat Jabatan Not Found"));

        riwayatJabatanRepository.delete(riwayatJabatan);
    }

    private RiwayatJabatanRes toRiwayatResponse(RiwayatJabatanEntity riwayatJabatan) {
        // Membangun JabatanRes untuk Jabatan Struktural
        JabatanRes jabatanRes = JabatanRes.builder()
                .id(riwayatJabatan.getId_jabatan().getId())
                .namaJabatan(riwayatJabatan.getId_jabatan().getNamaJabatan())
                .kodeJabatan(riwayatJabatan.getId_jabatan().getKodeJabatan())
                .isAtasan(riwayatJabatan.getId_jabatan().isAtasan())
                .sequence(riwayatJabatan.getId_jabatan().getSequence())
                .departement(riwayatJabatan.getId_jabatan().getDepartementEntity() != null
                        ? riwayatJabatan.getId_jabatan().getDepartementEntity().getDepartement_name() : null)
                .id_division(riwayatJabatan.getId_jabatan().getDivisionEntity() != null
                        ? riwayatJabatan.getId_jabatan().getDivisionEntity().getId() : null)
                .division(riwayatJabatan.getId_jabatan().getDivisionEntity() != null
                        ? riwayatJabatan.getId_jabatan().getDivisionEntity().getDivision_name() : null)
                .build();

        Long employeeNik = riwayatJabatanRepository.findEmployeeNikByRiwayatId(riwayatJabatan.getId());

        // Mengembalikan RiwayatJabatanRes yang menggunakan objek JabatanRes
        return RiwayatJabatanRes.builder()
                .id_riwayat(riwayatJabatan.getId())
                .statusKontrak(riwayatJabatan.getStatusKontrak())
                .tmt_awal(riwayatJabatan.getTmt_mulai())
                .tmt_akhir(riwayatJabatan.getTmt_akhir())
                .kontrakKedua(riwayatJabatan.getKontrakKedua())
                .salary(riwayatJabatan.getSalary())
                .kodeJabatan(jabatanRes)
                .employee_nik(employeeNik)
                .build();
    }
}