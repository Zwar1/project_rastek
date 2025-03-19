package com.example.userCrud.Dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRes {

    // 🔹 Informasi Personal
    private Long NIK;
    private String name;
    private Long idCard;
    private String noKtp;
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
    private String pendidikanTerakhir;
    private String jurusan;
    private String namaUniversitas;
    private String namaIbuKandung;
    private String statusPernikahan;
    private String jumlahAnak;
    private String nomorRekening;
    private String bank;

    // 🔹 Status & Metadata
    private LocalDate joinDate;
    private Boolean employeeStatus;
    private Date createdAt;
    private Date updatedAt;

    // 🔹 Informasi Absensi Terakhir
    private LocalDateTime lastCheckIn;
    private LocalDateTime lastCheckOut;
    private String lastWorkLocation;
    private Double lastLatitude;
    private Double lastLongitude;

    // 🔹 Relasi dengan Entity Lain
    private String userProfile;
    private List<RiwayatJabatanRes> riwayatJabatan;
    private List<AttachmentRes> attachment;
    private List<EmployeeAnnualRes> annuals;
    private List<LeaveRequestRes> leaveRequests;
}