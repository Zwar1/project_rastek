//package com.example.userCrud.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "device_log")
//public class DeviceLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", unique = true)
//    private Long id; // Unique ID for the log entry
//
//    @Column(name = "pin")
//    private String pin; // PIN karyawan
//
//    @Column(name = "check_in")
//    private LocalDateTime checkIn; // Waktu absen masuk
//
//    @Column(name = "check_out")
//    private LocalDateTime checkOut; // Waktu absen keluar
//
//    @Column(name = "attendance_status")
//    private String attendanceStatus; // Status kehadiran (Attend / Absent)
//
//    @Column(name = "total_hours")
//    private Double totalHours; // Total jam kerja
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private Date createdAt; // Waktu pembuatan log
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt; // Waktu pembaruan log
//
//}
