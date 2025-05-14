package com.example.userCrud.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PermissionReq {
    @NotBlank(message = "category cannot be empty")
    private String category;

    @NotBlank(message = "category cannot be empty")
    private String action;

    private String subCategory;
    private String description;
}
