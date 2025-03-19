package com.example.userCrud.Dto;

import com.example.userCrud.Entity.JabatanEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RiwayatJabatanRes {
    // Basic Information fields
    private Long id_riwayat;
    private String statusKontrak;
    private String tmt_awal;
    private String tmt_akhir;
    private String kontrakKedua;
    private BigDecimal salary;
    private JabatanRes kodeJabatan;
}
