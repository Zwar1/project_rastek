package com.example.userCrud.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class ManualAttendanceLogRequest {
    private Long idCard;
    private Long nik;
    private String fullName;
    private LocalDateTime manualCheckIn;
    private LocalDateTime manualCheckOut;
    private String attendanceStatus;
    private String manualTotalHours; // Ubah dari double ke String
    private String workLocation;
    private Double latitude;
    private Double longitude;
    private LocalDate date; //
    // 🔹 Tambahkan field baru untuk request log absensi bulanan
    private String monthYear; // Format "MM-yyyy"

    // Getter dan Setter untuk monthYear
    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    // Getter and Setter
    public Long getNik() {
        return nik;
    }

    public void setNik(Long nik) {
        this.nik = nik;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
