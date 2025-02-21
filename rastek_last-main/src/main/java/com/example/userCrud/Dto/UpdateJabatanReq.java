package com.example.userCrud.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class UpdateJabatanReq {

    @JsonIgnore
    @NotBlank
    private Long id;

    private String kodeJabatan;

    private String namaJabatan;

    private Boolean isAtasan;

    private Long sequence;

    // Department info
    private Long departementId; // ID dari DepartementEntity

    //     Division info
    private Long divisionId; // ID dari DivisionEntity
}