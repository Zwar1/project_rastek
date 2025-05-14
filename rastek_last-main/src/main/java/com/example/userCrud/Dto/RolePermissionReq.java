package com.example.userCrud.Dto;

import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Entity.Roles;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RolePermissionReq {
    @NotNull
    private Long roleId;

    @NotNull
    private Long permissionId;

    private boolean isAllowed = false;
}
