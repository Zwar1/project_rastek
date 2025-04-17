package com.example.userCrud.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateEmployeeKaryawanReq {
    // Request for update employee data (employee only)

    @JsonIgnore
    @NotNull
    private Long NIK;

    private String noTelp;
    private String kontakDarurat;
    private String noKontakDarurat;
    private String alamatDomisili;
    private String statusPernikahan;
    private String jumlahAnak;
}
