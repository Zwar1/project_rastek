package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeReq {
    // Personal Information fields
    @NotBlank
    private String name;

    @NotNull
    private Long idCard;

    @NotBlank
    private String no_ktp;

    @NotBlank
    private String NPWP;

    @NotBlank
    private String kartuKeluarga;

    @NotBlank
    private String jenisKelamin;

    @NotBlank
    private String tempatLahir;

    @NotNull
    private LocalDate tanggalLahir;

    @NotBlank
    private String agama;

    @NotBlank
    private String alamatLengkap;

    @NotBlank
    private String alamatDomisili;

    @NotBlank
    private String noTelp;

    @NotBlank
    private String kontakDarurat;

    @NotBlank
    private String noKontakDarurat;

    @NotBlank
    private String emailPribadi;

    @NotBlank
    private String pendidikanTerakhir;

    @NotBlank
    private String jurusan;

    @NotBlank
    private String namaUniversitas;

    @NotBlank
    private String namaIbuKandung;

    @NotBlank
    private String statusPernikahan;

    @NotBlank
    private String jumlahAnak;

    @NotBlank
    private String nomorRekening;

    @NotBlank
    private String bank;

    @NotNull
    private LocalDate joinDate;

    @NotNull
    private Boolean status;
}
