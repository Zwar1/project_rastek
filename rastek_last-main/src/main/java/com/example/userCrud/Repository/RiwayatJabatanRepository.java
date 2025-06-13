package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.JabatanEntity;
import com.example.userCrud.Entity.RiwayatJabatanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RiwayatJabatanRepository extends JpaRepository<RiwayatJabatanEntity, Long> {
    RiwayatJabatanEntity findFirstById(Long id);

    @Query(value = "SELECT e.NIK FROM employee e JOIN riwayat_jabatan rj ON e.NIK = rj.employee_nik WHERE rj.id_riwayat = :riwayatId", nativeQuery = true)
    Long findEmployeeNikByRiwayatId(@Param("riwayatId") Long riwayatId);

//    Optional<RiwayatJabatanEntity> findByEmployee(EmployeeEntity employee);
}