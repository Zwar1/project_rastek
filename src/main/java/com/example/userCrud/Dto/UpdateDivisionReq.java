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

public class UpdateDivisionReq {

    @JsonIgnore
    @NotBlank
    private Long id;

    @NotBlank
    private String division_name;

}
