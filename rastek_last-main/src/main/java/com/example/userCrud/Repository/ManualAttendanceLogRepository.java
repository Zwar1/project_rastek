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

    // Cari log absen pertama berdasarkan NIK yang sudah check-in tetapi belum check-out setelah waktu tertentu
    Optional<ManualAttendanceLog> findFirstByEmployeeNIKAndManualCheckInIsNotNullAndManualCheckOutIsNullAndManualCheckInAfter(
            Long nik, LocalDateTime checkInTime);

    // Ambil log absen terbaru berdasarkan NIK (Check-in terbaru)
    Optional<ManualAttendanceLog> findTopByEmployeeNIKOrderByManualCheckInDesc(Long nik);

    // Ambil semua log absensi
    List<ManualAttendanceLog> findAll();

    // Ambil log absensi berdasarkan NIK
    List<ManualAttendanceLog> findByEmployeeNIK(Long nik);

    // Ambil log absensi setelah waktu tertentu berdasarkan NIK
    List<ManualAttendanceLog> findByEmployeeNIKAndManualCheckInAfter(Long nik, LocalDateTime date);

    // Ambil log absensi dalam rentang waktu tertentu berdasarkan NIK
    List<ManualAttendanceLog> findByEmployeeNIKAndManualCheckInBetween(
            Long nik, LocalDateTime startDate, LocalDateTime endDate);

    // Cek apakah ada data absensi berdasarkan NIK
    boolean existsByEmployeeNIK(Long nik);

    // Ambil log absensi dalam rentang waktu tertentu
    List<ManualAttendanceLog> findByManualCheckInBetween(LocalDateTime start, LocalDateTime end);

    // Check-in pertama di hari tertentu
    @Query("SELECT m.manualCheckIn FROM ManualAttendanceLog m WHERE m.employee.NIK = :nik " +
            "AND FUNCTION('DATE', m.manualCheckIn) = :date ORDER BY m.manualCheckIn ASC")
    Optional<LocalDateTime> findFirstCheckIn(@Param("nik") Long nik, @Param("date") LocalDate date);

    // Check-out terakhir di hari tertentu
    @Query("SELECT m.manualCheckOut FROM ManualAttendanceLog m WHERE m.employee.NIK = :nik " +
            "AND m.manualCheckOut IS NOT NULL AND FUNCTION('DATE', m.manualCheckOut) = :date " +
            "ORDER BY m.manualCheckOut DESC")
    Optional<LocalDateTime> findLastCheckOut(@Param("nik") Long nik, @Param("date") LocalDate date);

    // Ambil log absensi pertama setelah waktu tertentu berdasarkan NIK
    Optional<ManualAttendanceLog> findFirstByEmployeeNIKAndManualCheckInAfter(
            @Param("nik") Long nik, @Param("checkInTime") LocalDateTime checkInTime);

    // Cari log absensi berdasarkan NIK dan Tanggal
    Optional<ManualAttendanceLog> findFirstByEmployeeNIKAndDate(Long nik, LocalDate date);


}
