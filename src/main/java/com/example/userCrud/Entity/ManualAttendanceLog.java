package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manual_attendance_log")
public class ManualAttendanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin", unique = true)
    private Long pin; // Unique ID for the log entry

    // Relasi dengan EmployeeEntity menggunakan NIK
    @ManyToOne(fetch = FetchType.LAZY) // Gunakan LAZY untuk efisiensi query
    @JoinColumn(name = "nik", referencedColumnName = "NIK", nullable = false)
    private EmployeeEntity employee; // Relasi dengan EmployeeEntity berdasarkan NIK

    @Column(name = "employee_name", nullable = true) // Nullable untuk menghindari constraint issue
    private String employeeName;

    @Column(name = "manual_check_in")
    private LocalDateTime manualCheckIn; // Manual check-in time

    @Column(name = "manual_check_out")
    private LocalDateTime manualCheckOut; // Manual check-out time

    @Column(name = "attendance_status")
    private String attendanceStatus; // Attendance status (Attend / Absent)

    @Column(name = "manual_total_hours")
    private String manualTotalHours; // Total hours worked manually

    @Column(name = "work_location")
    private String workLocation; // Work location (Office / WFH)

    @Column(name = "latitude", nullable = true)
    private Double latitude; // Latitude of the check-in location

    @Column(name = "longitude", nullable = true)
    private Double longitude; // Longitude of the check-in location

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Log creation time

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Log update time

    /**
     * Mengisi employeeName secara otomatis sebelum data disimpan atau diperbarui.
     */
    @PrePersist
    @PreUpdate
    private void preSave() {
        if (this.employee != null && (this.employeeName == null || this.employeeName.isEmpty())) {
            this.employeeName = this.employee.getName();
        }
    }

    /**
     * Setter untuk employee agar langsung mengisi employeeName.
     */
    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
        if (employee != null) {
            this.employeeName = employee.getName();
        }
    }
}
