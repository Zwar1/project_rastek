package com.example.userCrud.Service;

import com.example.userCrud.Entity.AttendanceLog;
import com.example.userCrud.Repository.AttendanceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceLogService {

    @Autowired
    private AttendanceLogRepository attendanceRepository;

    // Mendapatkan log kehadiran berdasarkan PIN
    public List<AttendanceLog> getAttendanceLogs(String pin) {
        // Mendapatkan semua log untuk PIN tersebut dari database
        List<AttendanceLog> logs = attendanceRepository.findByPin(pin);

        // Menyaring dan memproses log berdasarkan tanggal hari ini dan status
        List<AttendanceLog> processedLogs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate todayDate = LocalDate.now();

        for (AttendanceLog log : logs) {
            String logDate = log.getDatetime().substring(0, 10);  // Mengambil tanggal dari datetime
            if (todayDate.toString().equals(logDate)) {
                // Log hanya untuk tanggal hari ini
                processedLogs.add(log);
            }
        }

        // Proses data log untuk menghitung status dan total hours
        for (AttendanceLog log : processedLogs) {
            // Proses status kehadiran
            String status = (log.getStatus().equals("0")) ? "Attend" : "Absent";
            log.setStatus(status);

            // Hitung total jam berdasarkan log Absen Masuk dan Absen Keluar
            // Misalnya, jika ada Absen Masuk dan Absen Keluar
            if (log.getStatus().equals("Attend")) {
                String checkIn = log.getDatetime();  // Ambil Absen Masuk
                String checkOut = ""; // Ambil Absen Keluar jika ada
                double totalHours = 0.0;

                // Logika perhitungan jam jika Absen Keluar lebih besar dari Absen Masuk
                if (!checkOut.isEmpty()) {
                    // Perhitungan jam
                    totalHours = calculateTotalHours(checkIn, checkOut);
                    log.setDatetime(String.valueOf(totalHours));  // Mengupdate field datetime dengan total jam
                }
            }
        }

        return processedLogs;
    }

    // Fungsi untuk menghitung total jam berdasarkan waktu masuk dan keluar
    private double calculateTotalHours(String checkIn, String checkOut) {
        // Implementasikan perhitungan jam di sini, menggunakan java.time
        return 0.0;  // Placeholder
    }
}
