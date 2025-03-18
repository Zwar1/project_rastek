package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CompanyEventRepository extends JpaRepository<CompanyEvent, Long> {
    Optional<CompanyEvent> findFirstById(Long id);
    
}
