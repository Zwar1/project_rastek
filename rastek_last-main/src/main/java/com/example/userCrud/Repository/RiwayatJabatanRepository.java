package com.example.userCrud.Repository;

import com.example.userCrud.Entity.RiwayatJabatanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RiwayatJabatanRepository extends JpaRepository<RiwayatJabatanEntity, Long> {
    RiwayatJabatanEntity findFirstById(Long id);

//    Optional<RiwayatJabatanEntity> findByEmployee(EmployeeEntity employee);
}
