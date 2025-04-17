package com.example.userCrud.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManualAttendanceLogResponse {

    private Long id; // ID dari log absensi
    private Long employeeNIK; // NIK karyawan
    private String fullName; // Nama lengkap karyawan

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime manualCheckIn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime manualCheckOut;

    private String attendanceStatus; // Status kehadiran (e.g., "Present", "Absent")
    private String manualTotalHours; // Format total jam kerja menjadi "X Jam Y Menit"
    private String workLocation; // Lokasi kerja (e.g., "Office", "WFH")

    private Double latitude; // Koordinat Latitude untuk absensi
    private Double longitude; // Koordinat Longitude untuk absensi

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date; // Tanggal absensi
}
