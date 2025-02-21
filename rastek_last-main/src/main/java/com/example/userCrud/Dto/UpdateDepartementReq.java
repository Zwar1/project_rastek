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

public class UpdateDepartementReq {

    @JsonIgnore
    @NonNull
    private Long id;

    @NotBlank
    private String departement_name;

    @NotBlank
    private String departement_head;

}
