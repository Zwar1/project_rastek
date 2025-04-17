package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.JabatanEntity;
import com.example.userCrud.Entity.ManualAttendanceLog;
import com.example.userCrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findFirstByNIK(Long NIK);

    // Check if Employee NIK exists
    boolean existsByNIK(Long NIK);

    // Count employees who joined in a specific month and year
    long countByJoinDateBetween(LocalDate startDate, LocalDate endDate);

    // Find Employee by associated User
    Optional<EmployeeEntity> findByUser(User user);

    // Find Employee by name
    Optional<EmployeeEntity> findByName(String name);

    List<EmployeeEntity> findByNIKIn(List<Long> nikList);

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
    @Query("UPDATE EmployeeEntity e SET e.lastCheckIn = :checkIn, e.lastCheckOut = :checkOut, " +
            "e.lastWorkLocation = :workLocation, e.lastLatitude = :latitude, " +
            "e.lastLongitude = :longitude, e.attendanceStatus = :attendanceStatus " +
            "WHERE e.NIK = :nik")
    void updateLastAttendance(@Param("nik") Long nik,
                              @Param("checkIn") LocalDateTime checkIn,
                              @Param("checkOut") LocalDateTime checkOut,
                              @Param("workLocation") String workLocation,
                              @Param("latitude") Double latitude,
                              @Param("longitude") Double longitude,
                              @Param("attendanceStatus") String attendanceStatus);

    // Get the latest ManualAttendanceLog by Employee NIK
    @Query("SELECT l FROM ManualAttendanceLog l WHERE l.employee.NIK = :nik ORDER BY l.manualCheckIn DESC")
    Optional<ManualAttendanceLog> findLatestAttendanceLogByNIK(@Param("nik") Long NIK);

    // Get all attendance logs by Employee NIK
    @Query("SELECT l FROM ManualAttendanceLog l WHERE l.employee.NIK = :nik ORDER BY l.manualCheckIn DESC")
    List<ManualAttendanceLog> findAllAttendanceLogsByNIK(@Param("nik") Long NIK);
    // pin employee
    Optional<EmployeeEntity> findByPin(String pin);
}