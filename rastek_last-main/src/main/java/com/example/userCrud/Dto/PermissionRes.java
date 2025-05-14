package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionRes {
    private Long id;
    private String category;
    private String action;
    private String subCategory;
    private String description;
    private String permissionKey;
}
