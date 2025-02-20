package com.example.userCrud.Dto;

import com.example.userCrud.Entity.Attachment;
import com.example.userCrud.Entity.RiwayatJabatanEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class EmployeeRes {

    // Personal Information fields
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
    private String pendidikanTerakhir;
    private String jurusan;
    private String namaUniversitas;
    private String namaIbuKandung;
    private String statusPernikahan;
    private String jumlahAnak;
    private String nomorRekening;
    private String bank;
    private LocalDate joinDate;
    private Boolean status;
    private Date created_at;
    private Date updated_at;
    private String userProfile;
    private List<RiwayatJabatanRes> riwayatJabatan;
    private List<AttachmentRes> attachment;


    // 🔹 Informasi Absensi Terakhir
    private LocalDateTime lastCheckIn;
    private LocalDateTime lastCheckOut;
    private String lastWorkLocation;
    private Double lastLatitude;
    private Double lastLongitude;

    private List<CVRes> cv;
}
