package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRoleRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public String getName() {
        if (name != null && !name.startsWith("ROLE_")) {
            return "ROLE_" + name;
        }
        return name;
    }
}
