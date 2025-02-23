package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeAnnual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeAnnualRepository extends JpaRepository<EmployeeAnnual, Long>{
    Optional<EmployeeAnnual> findFirstById(Long id);
//
//    Optional<EmployeeAnnual> findByNamaCuti(String namaCuti);
}
