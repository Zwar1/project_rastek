package com.example.userCrud.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JabatanRes {

    //Struktural Fields
    private Long id;
    private Long id_division;
    private String kodeJabatan;
    private String namaJabatan;
    private Boolean isAtasan;
    private Long sequence;
    private String departement;
    private String division;
}