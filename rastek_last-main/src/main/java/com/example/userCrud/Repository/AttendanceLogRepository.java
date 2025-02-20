package com.example.userCrud.Repository;

import com.example.userCrud.Entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, String> {

    // Menemukan semua log kehadiran berdasarkan PIN
    List<AttendanceLog> findByPin(String pin);

}
