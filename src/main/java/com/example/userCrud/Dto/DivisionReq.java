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

public class DivisionReq {

    @NotBlank
    private String division_name;

    @NotNull
    private Long departementId;
}
