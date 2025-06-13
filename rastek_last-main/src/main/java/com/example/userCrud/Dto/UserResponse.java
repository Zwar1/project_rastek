package com.example.userCrud.Dto;

import java.util.Date;
import java.util.List;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Date created_at;
    private Date updated_at;
    private List<RolesSimpleRes> roles;
    private List<RolePermissionRes> permissions;
    private String userProfile;
    private EmployeeRes employee;
    private String created_by;
    private String updated_by;
    private Boolean is_active;
    private Boolean is_deleted;

    public String getUpdated_by() {
        if (updated_by == null) {
            return updated_by = "There is no update yet";
        }
        return updated_by;
    }
}