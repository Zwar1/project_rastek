package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    // Find Employee by NIK
    Optional<EmployeeEntity> findFirstByNIK(Long NIK);

    // Count employees who joined in a specific month and year
    long countByJoinDateBetween(LocalDate startDate, LocalDate endDate);

    // Find Employee by associated User
    Optional<EmployeeEntity> findByUser(User user);

    // Find Employee by name
    Optional<EmployeeEntity> findByName(String name);

    // Get the latest employee by created date
    Optional<EmployeeEntity> findTopByOrderByCreatedAtDesc();

    // Reset lastCheckIn and lastCheckOut to NULL for all employees
    @Modifying
    @Transactional
    @Query("UPDATE EmployeeEntity e SET e.lastCheckIn = NULL, e.lastCheckOut = NULL")
    void resetCheckInCheckOut();
    // Update lastCheckIn and lastCheckOut based on NIK
    @Modifying
    @Transactional
    @Query("UPDATE EmployeeEntity e SET e.lastCheckIn = :checkIn, e.lastCheckOut = :checkOut WHERE e.NIK = :nik")
    void updateCheckInCheckOut(@Param("nik") Long nik, @Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut);
}
