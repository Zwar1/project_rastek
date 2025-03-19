package com.example.userCrud.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LeadsReq {


    @NotBlank
    private String clientName;

    @NotBlank
    private String clientNumber;

    @NotBlank
    @Email
    private String clientEmail;
}
