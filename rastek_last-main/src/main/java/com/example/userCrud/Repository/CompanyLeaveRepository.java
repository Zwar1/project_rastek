package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Entity.DepartementEntity;
import com.example.userCrud.Entity.JabatanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyLeaveRepository extends JpaRepository<CompanyLeave, Long> {
    Optional<CompanyLeave> findFirstById(Long id);

    Optional<CompanyLeave> findAllById(Long id);

    Optional<CompanyLeave> findByJenisCuti(String jenisCuti);
}
