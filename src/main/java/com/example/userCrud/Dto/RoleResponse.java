package com.example.userCrud.Dto;

import java.util.Date;

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
    private String description;
    private Date created_at;
    private Date updated_at;
    private String created_by;
    private String updated_by;

    public String getUpdated_by() {
        if (updated_by == null) {
            return updated_by = "There is no update yet";
        }
        return updated_by;
    }

}
