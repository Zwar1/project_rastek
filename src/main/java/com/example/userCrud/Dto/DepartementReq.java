package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class DepartementReq {

    @NotBlank
    private String departement_name;


    private String departement_head;

}
