//package com.example.userCrud.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "attendance_log")
//public class AttendanceLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_nik", nullable = false)
//    private EmployeeEntity employee;  // Relasi dengan entitas Employee, sesuaikan dengan nama entitas karyawan Anda
//
//    private LocalDateTime clockIn;
//    private LocalDateTime clockOut;
//
//    private String status;  // Added for status (attendance status)
//    private String pin;     // Added for PIN
//
//}
