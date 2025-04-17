package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CVEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CVEntity, Long> {
    void deleteByEmployee_NIK(Long nik);
}