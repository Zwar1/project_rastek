package com.example.userCrud.Repository;


import com.example.userCrud.Entity.JabatanEntity;
import com.example.userCrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JabatanRepository extends JpaRepository<JabatanEntity, String> {
    JabatanEntity findFirstByKodeJabatan(String kodeJabatan);
    Optional<JabatanEntity> findByNamaJabatan(String namaJabatan);

    @Query("SELECT j.namaJabatan FROM JabatanEntity j WHERE j.departementEntity.id = :departementId AND j.isAtasan = :isAtasan")
    List<String> findNamaJabatanByDepartementAndIsAtasan(@Param("departementId") Long departementId, @Param("isAtasan") boolean isAtasan);

}
