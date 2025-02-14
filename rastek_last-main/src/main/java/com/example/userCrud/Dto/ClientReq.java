package com.example.userCrud.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientReq {
    private String clientName;

    private String clientContact;

    @Email
    private String clientEmail;

    private String clientCountry;

    private Boolean isActive;
}
