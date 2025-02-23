package com.example.userCrud.Repository;

import com.example.userCrud.Entity.ManualAttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ManualAttendanceLogRepository extends JpaRepository<ManualAttendanceLog, Long> {

    Optional<ManualAttendanceLog> findFirstByEmployeeNIKAndManualCheckInIsNotNullAndManualCheckOutIsNullAndManualCheckInAfter(
            Long nik, LocalDateTime checkInTime);

    Optional<ManualAttendanceLog> findTopByEmployeeNIKOrderByManualCheckInDesc(Long nik); // Perbaikan

    List<ManualAttendanceLog> findAll();

    List<ManualAttendanceLog> findByEmployeeNIK(Long nik);

    List<ManualAttendanceLog> findByEmployeeNIKAndManualCheckInAfter(Long nik, LocalDateTime date);

    List<ManualAttendanceLog> findByEmployeeNIKAndManualCheckInBetween(
            Long nik, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByEmployeeNIK(Long nik);

    List<ManualAttendanceLog> findByManualCheckInBetween(LocalDateTime start, LocalDateTime end);

    // Check-in pertama di hari tertentu
    @Query("SELECT m.manualCheckIn FROM ManualAttendanceLog m WHERE m.employee.NIK = :nik " +
            "AND DATE(m.manualCheckIn) = :date ORDER BY m.manualCheckIn ASC")
    Optional<LocalDateTime> findFirstCheckIn(@Param("nik") Long nik, @Param("date") LocalDate date);

    // Check-out terakhir di hari tertentu
    @Query("SELECT m.manualCheckOut FROM ManualAttendanceLog m WHERE m.employee.NIK = :nik " +
            "AND m.manualCheckOut IS NOT NULL AND DATE(m.manualCheckOut) = :date " +
            "ORDER BY m.manualCheckOut DESC")
    Optional<LocalDateTime> findLastCheckOut(@Param("nik") Long nik, @Param("date") LocalDate date);
    Optional<ManualAttendanceLog> findFirstByEmployeeNIKAndManualCheckInAfter(
            @Param("nik") Long nik, @Param("checkInTime") LocalDateTime checkInTime);
}
