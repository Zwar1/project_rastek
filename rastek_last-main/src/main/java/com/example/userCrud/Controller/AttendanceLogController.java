package com.example.userCrud.Controller;

import com.example.userCrud.Entity.AttendanceLog;
import com.example.userCrud.Service.AttendanceLogService;
import com.example.userCrud.Controller.AttendanceLogController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendanceLogController {

    @Autowired
    private AttendanceLogService attendanceLogServiceService;

    // Endpoint untuk mengambil log kehadiran berdasarkan PIN
    @GetMapping("/attendance")
    public List<AttendanceLog> getAttendanceLogs(@RequestParam String pin) {
        // Memanggil service untuk mendapatkan log kehadiran dan menghitung total jam
        return attendanceLogServiceService.getAttendanceLogs(pin);
    }
}
