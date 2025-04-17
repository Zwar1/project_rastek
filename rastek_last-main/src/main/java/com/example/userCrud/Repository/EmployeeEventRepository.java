package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyEvent;
import com.example.userCrud.Entity.EmployeeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeEventRepository extends JpaRepository<EmployeeEvent, Long> {
    Optional<EmployeeEvent> findFirstById(Long id);

    boolean existsByIsCutiTrue();
}
