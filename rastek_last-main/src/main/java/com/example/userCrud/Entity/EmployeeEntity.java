package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @Column(name = "NIK", unique = true)
    private Long NIK;

    private String name;
    private Long idCard;
    private String no_ktp;
    private String NPWP;
    private String kartuKeluarga;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String agama;
    private String alamatLengkap;
    private String alamatDomisili;
    private String noTelp;
    private String kontakDarurat;
    private String noKontakDarurat;
    private String emailPribadi;
    private String jurusan;
    private String namaUniversitas;
    private String namaIbuKandung;
    private String statusPernikahan;
    private String jumlahAnak;
    private String nomorRekening;
    private String bank;
    private String pendidikanTerakhir;

    private LocalDate joinDate;

    private Boolean employeeStatus;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "pin")
    private String pin;

    @Column(name = "last_check_in")
    private LocalDateTime lastCheckIn;

    @Column(name = "last_check_out")
    private LocalDateTime lastCheckOut;

    @Column(name = "last_work_location")
    private String lastWorkLocation;

    @Column(name = "last_latitude")
    private Double lastLatitude;

    @Column(name = "last_longitude")
    private Double lastLongitude;

    // Relasi OneToMany dengan Riwayat Jabatan
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_nik", referencedColumnName = "NIK", nullable = false)
    private List<RiwayatJabatanEntity> riwayatJabatan = new ArrayList<>();

    // Relasi OneToOne dengan User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = true)
    private User user;

    // Relasi OneToMany dengan Attachment
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_attachment", nullable = true)
    private List<Attachment> attachment = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_annual", nullable = true)
    private List<EmployeeAnnual> annuals = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_requestCuti", nullable = true)
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    // Relasi OneToOne dengan UserProfile
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_profile", referencedColumnName = "id", nullable = true)
    private UserProfile userProfile;

    // Relasi OneToMany dengan Riwayat Jabatan
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_attendance", referencedColumnName = "NIK", nullable = false)
    private List<AttendanceLog> attendanceLogs = new ArrayList<>();

    // Relasi OneToMany dengan CV
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CVEntity> cv = new ArrayList<>();
}
