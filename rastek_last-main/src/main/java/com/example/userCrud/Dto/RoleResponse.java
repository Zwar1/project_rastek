package com.example.userCrud.Dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Entity.RolePermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private Long id;

    private String name;
    private String status;
    private Set<PermissionsEntity> permissions;
    private Integer version;
    private String created_by;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
