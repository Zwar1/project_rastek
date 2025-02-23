//package com.example.userCrud.Repository;
//
//import com.example.userCrud.Entity.DeviceLog;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {
//
//    // Query untuk mencari log berdasarkan PIN
//    List<DeviceLog> findByPin(String pin);
//
//    // Query untuk mencari log berdasarkan status
//    List<DeviceLog> findByAttendanceStatus(String status);  // Disesuaikan dengan field yang ada pada entitas
//
//    // Query untuk mencari log berdasarkan PIN dan rentang waktu
//    List<DeviceLog> findByPinAndCheckInBetween(String pin, LocalDateTime startDate, LocalDateTime endDate);  // Disesuaikan dengan field checkIn
//}
