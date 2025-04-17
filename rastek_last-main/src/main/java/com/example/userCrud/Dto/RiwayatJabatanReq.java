package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RiwayatJabatanReq {
    // Basic Information fields

    @NotNull
    private Long NIK;

    @NotBlank
    private String statusKontrak;

    @NotBlank
    private String tmt_awal;

    @NotBlank
    private String tmt_akhir;

    private String kontrakKedua;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private String namaJabatan;

    @NotNull
    private Long idJabatan;
}
