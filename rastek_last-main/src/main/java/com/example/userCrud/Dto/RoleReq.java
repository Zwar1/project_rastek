package com.example.userCrud.Dto;

import com.example.userCrud.Entity.RolePermissionEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoleReq {
    @NotBlank(message = "Role name is required")
    private String name;

    private String status;
//    private Set<RolePermissionEntity> permissions;
}
