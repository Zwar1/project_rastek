//package com.example.userCrud.Entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "attendance_log")
//@Getter
//@Setter
//public class AttendanceLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // ID Otomatis
//
//    @Column(nullable = false)
//    private String pin; // PIN Karyawan
//
//    @Column(nullable = false)
//    private LocalDateTime dateTime; // Waktu Absensi
//
//    @Column(nullable = false)
//    private String status; // Status (Check-in, Check-out, dll.)
//}
