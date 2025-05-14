package com.example.userCrud.Dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoleCreateReq {
    private RoleReq roleReq;
    private Set<Long> permissionIds;
}
