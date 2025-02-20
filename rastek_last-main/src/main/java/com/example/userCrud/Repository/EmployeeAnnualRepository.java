package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeAnnual;
import com.example.userCrud.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeAnnualRepository extends JpaRepository<EmployeeAnnual, Long>{
    Optional<EmployeeAnnual> findFirstById(Long id);

    Optional<EmployeeAnnual> findByEmployee(EmployeeEntity employee);

//    List<EmployeeAnnual> findAllByEmployeeId(Long employeeId);

    List<EmployeeAnnual> findAllByEmployee(EmployeeEntity employee);
//
//    Optional<EmployeeAnnual> findByNamaCuti(String namaCuti);
}
