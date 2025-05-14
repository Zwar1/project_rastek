package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermissionRes {
  //  private Long id;
    private Long rolesId;
    private Long permissionsId;
    private boolean isAllowed;
    private String permissionKey;
}
