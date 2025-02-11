package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CVReq {
    @NotNull
    private Long NIK;

    @NotBlank
    private String projectName;

    @NotBlank
    private String projectRole;

    @NotNull
    private LocalDate projectStart;

    @NotNull
    private LocalDate projectEnd;

    @NotBlank
    private String projectDescription;
}
