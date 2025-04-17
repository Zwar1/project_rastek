package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "manual_attendance_log")
public class ManualAttendanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin", unique = true)
    private Long pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nik", referencedColumnName = "NIK", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "employee_name", nullable = true)
    private String employeeName;

    @Column(name = "manual_check_in")
    private LocalDateTime manualCheckIn;

    @Column(name = "manual_check_out")
    private LocalDateTime manualCheckOut;

    @Column(name = "attendance_status")
    private String attendanceStatus;

    @Column(name = "manual_total_hours")
    private String manualTotalHours;

    @Column(name = "work_location")
    private String workLocation;

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @PrePersist
    @PreUpdate
    private void preSave() {
        if (this.employee != null && (this.employeeName == null || this.employeeName.isEmpty())) {
            this.employeeName = this.employee.getName();
        }

        if (this.manualCheckIn != null && this.manualCheckOut == null) {
            if (LocalDateTime.now().isAfter(this.manualCheckIn.withHour(17).withMinute(0).withSecond(0))) {
                if ("NOT CHECKED IN".equals(this.attendanceStatus)) {
                    this.attendanceStatus = "ABSENT";
                }
                if (this.manualCheckOut == null) {
                    this.manualCheckOut = LocalDateTime.now();
                    long hoursWorked = ChronoUnit.HOURS.between(this.manualCheckIn, this.manualCheckOut);
                    long minutesWorked = ChronoUnit.MINUTES.between(this.manualCheckIn, this.manualCheckOut) % 60;
                    this.manualTotalHours = String.format("%d hours %d minutes", hoursWorked, minutesWorked);
                    if ("LATE".equals(this.attendanceStatus)) {
                        this.attendanceStatus = "LATE";
                    } else {
                        this.attendanceStatus = "PRESENT";
                    }
                }
            }
        } else if (this.manualCheckIn == null && "NOT CHECKED IN".equals(this.attendanceStatus)) {
            if (LocalDateTime.now().isAfter(LocalDateTime.now().withHour(17).withMinute(0).withSecond(0))) {
                this.attendanceStatus = "ABSENT";
            }
        }
    }
}
