package com.example.userCrud.Service;


import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;


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

        if (request.getKodeJabatan() != null) {
            JabatanEntity jabatanEntityS = jabatanRepository.findFirstByKodeJabatan(request.getKodeJabatan())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));
            riwayatJabatan.setKode_jabatan(jabatanEntityS);
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

        if (request.getKodeJabatan() != null) {
            JabatanEntity jabatanEntityS = jabatanRepository.findById(request.getKodeJabatan())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jabatan Not Found"));
            riwayatJabatan.setKode_jabatan(jabatanEntityS);

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
                .namaJabatan(riwayatJabatan.getKode_jabatan().getNamaJabatan())
                .kodeJabatan(riwayatJabatan.getKode_jabatan().getKodeJabatan())
                .isAtasan(riwayatJabatan.getKode_jabatan().isAtasan())
                .sequence(riwayatJabatan.getKode_jabatan().getSequence())
                .departement(riwayatJabatan.getKode_jabatan().getDepartementEntity() != null
                        ? riwayatJabatan.getKode_jabatan().getDepartementEntity().getDepartement_name() : null)
                .division(riwayatJabatan.getKode_jabatan().getDivisionEntity() != null
                        ? riwayatJabatan.getKode_jabatan().getDivisionEntity().getDivision_name() : null)
                .build();

        // Mengembalikan RiwayatJabatanRes yang menggunakan objek JabatanRes
        return RiwayatJabatanRes.builder()
                .id_riwayat(riwayatJabatan.getId())
                .statusKontrak(riwayatJabatan.getStatusKontrak())
                .tmt_awal(riwayatJabatan.getTmt_mulai())
                .tmt_akhir(riwayatJabatan.getTmt_akhir())
                .kontrakKedua(riwayatJabatan.getKontrakKedua())
                .salary(riwayatJabatan.getSalary())
                .kodeJabatan(jabatanRes)
                .build();
    }



}
