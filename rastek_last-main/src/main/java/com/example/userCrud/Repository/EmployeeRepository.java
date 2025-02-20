package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findFirstByNIK(Long NIK);

    // Method untuk menghitung jumlah karyawan yang mendaftar pada bulan dan tahun tertentu
    long countByJoinDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<EmployeeEntity> findByUser(User user);

    Optional<EmployeeEntity> findByName(String name);

    Optional<EmployeeEntity> findTopByOrderByCreatedAtDesc();

    List<EmployeeEntity> findByNIKIn(List<Long> nikList);
}