package com.example.userCrud.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UpdateRiwayatJabatanReq {

    @JsonIgnore
    @NotBlank
    private Long id;

    // Basic Information fields
    private String statusKontrak;
    private String tmt_awal;
    private String tmt_akhir;
    private String kontrakKedua;
    private BigDecimal salary;

    // Department info
    private Long departementId;

    //     Division info
    private Long divisionId;

    //     SubDivision info
    private Long subDivisionId;

    private String kodeJabatan;
}
