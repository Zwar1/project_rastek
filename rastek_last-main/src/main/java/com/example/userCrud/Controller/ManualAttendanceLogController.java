package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ManualAttendanceLogRequest;
import com.example.userCrud.Dto.ManualAttendanceLogResponse;
import com.example.userCrud.Entity.ManualAttendanceLog;
import com.example.userCrud.Service.ManualAttendanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class ManualAttendanceLogController {

    @Autowired
    private ManualAttendanceLogService manualAttendanceLogService;

    // Endpoint untuk manual check-in menggunakan body
    @PostMapping("/check-in")
    public ResponseEntity<ManualAttendanceLogResponse> manualCheckIn(@RequestBody ManualAttendanceLogRequest checkInRequest) {
        System.out.println("Received Check-in Request: " + checkInRequest);

        if (checkInRequest == null) {
            System.out.println("Request body is null!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        System.out.println("NIK: " + checkInRequest.getNik());
        System.out.println("Work Location: " + checkInRequest.getWorkLocation());
        System.out.println("Latitude: " + checkInRequest.getLatitude());
        System.out.println("Longitude: " + checkInRequest.getLongitude());
        System.out.println("Received Check-in Request: " + checkInRequest.toString());
        System.out.println("Checking for existing check-in with NIK: " + checkInRequest.getNik() + " and Date: " + checkInRequest.getDate());

        try {
            // Here, we now expect a ManualAttendanceLogResponse instead of ManualAttendanceLog
            ManualAttendanceLogResponse response = manualAttendanceLogService.manualCheckIn(
                    checkInRequest.getNik(),
                    checkInRequest.getWorkLocation(),
                    checkInRequest.getLatitude(),
                    checkInRequest.getLongitude());

            System.out.println("Check-in successful: " + response);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();  // Log full error
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint untuk manual check-out
    @PostMapping("/check-out")
    public ResponseEntity<ManualAttendanceLogResponse> manualCheckOut(@RequestParam Long nik) {
        try {
            // Call the service to perform the manual checkout
            ManualAttendanceLogResponse response = manualAttendanceLogService.manualCheckOut(nik);

            // Return the response wrapped in ResponseEntity with HTTP status OK
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle any errors, e.g., log not found, invalid state
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 🔹 API untuk mendapatkan semua log absensi berdasarkan NIK
    @GetMapping("/logs")
    public ResponseEntity<?> getAttendanceLogsByNIK(@RequestParam Long nik) {
        try {
            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getAllAttendanceLogsByNIK(nik);
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan pada server.");
        }
    }

    // 🔹 API untuk mendapatkan log absensi dalam rentang tanggal tertentu berdasarkan NIK
    @GetMapping("/logs/range")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getRangeAttendanceLogs(
            @RequestParam Long nik,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getAttendanceLogs(nik, startDate, endDate);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 🔹 API untuk mendapatkan log absensi mingguan berdasarkan NIK
    @GetMapping("/logs/weekly")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getWeeklyAttendanceLogs(@RequestParam Long nik) {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now();

        return ResponseEntity.ok(manualAttendanceLogService.getAttendanceLogs(nik, startDate, endDate));
    }

    @PostMapping("/logs/monthly")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getMonthlyAttendanceLogs(
            @RequestBody ManualAttendanceLogRequest request) {
        try {
            System.out.println("Received Request: " + request);

            if (request == null || request.getNik() == null || request.getMonthYear() == null) {
                System.out.println("Invalid request data: NIK or Month-Year is missing.");
                return ResponseEntity.badRequest().body(null);
            }

            // 🔹 Pisahkan `monthYear` menjadi `month` dan `year`
            String[] parts = request.getMonthYear().split("-");
            if (parts.length != 2) {
                System.out.println("Invalid monthYear format. Expected format MM-yyyy.");
                return ResponseEntity.badRequest().body(null);
            }

            int month;
            int year;
            try {
                month = Integer.parseInt(parts[0]);  // MM (Bulan)
                year = Integer.parseInt(parts[1]);   // yyyy (Tahun)
            } catch (NumberFormatException e) {
                System.out.println("Error parsing month or year.");
                return ResponseEntity.badRequest().body(null);
            }

            if (month < 1 || month > 12) {
                System.out.println("Invalid month value: " + month);
                return ResponseEntity.badRequest().body(null);
            }

            // Tentukan tanggal awal dan akhir dari bulan yang diminta
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            System.out.println("Fetching logs for NIK: " + request.getNik() +
                    " From: " + startDate + " To: " + endDate);

            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getAttendanceLogs(
                    request.getNik(), startDate, endDate);

            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // 🔹 API untuk mendapatkan log absensi hari ini
    @GetMapping("/logs/today")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getTodayAttendanceLogs() {
        try {
            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getTodayAttendanceLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 🔹 API untuk mendapatkan semua log absensi dalam rentang tanggal tertentu
    @GetMapping("/logs/all")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getAllAttendanceLogsInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getAllAttendanceLogsInRange(startDate, endDate);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 🔹 API untuk mendapatkan semua log absensi dalam rentang **mingguan**
    @GetMapping("/logs/all/weekly")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getAllWeeklyAttendanceLogs() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now();

        return ResponseEntity.ok(manualAttendanceLogService.getAllAttendanceLogsInRange(startDate, endDate));
    }

    // 🔹 API untuk mendapatkan semua log absensi dalam rentang **bulanan**
    @GetMapping("/logs/all/monthly")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getAllMonthlyAttendanceLogs() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();

        return ResponseEntity.ok(manualAttendanceLogService.getAllAttendanceLogsInRange(startDate, endDate));
    }

    // 🔹 API untuk mendapatkan semua log absensi tanpa filter tanggal
    @GetMapping("/logs/data")
    public ResponseEntity<List<ManualAttendanceLogResponse>> getAllAttendanceLogs() {
        try {
            List<ManualAttendanceLogResponse> logs = manualAttendanceLogService.getPureAllAttendanceLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}







