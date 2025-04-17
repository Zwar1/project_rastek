package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class JabatanReq {

    //Struktural Fields
    @NotBlank
    private String kodeJabatan;

    @NotBlank
    private String namaJabatan;

    private Boolean isAtasan;

    @NotNull
    private Long sequence;

    // Department info
    @NotNull
    private Long departementId; // ID dari DepartementEntity

    //     Division inf
    private Long divisionId; // ID dari DivisionEntity

}
