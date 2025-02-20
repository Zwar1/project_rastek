package com.example.userCrud.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeadsRes {

    private Long id;

    private String clientName;

    private String clientNumber;

    private boolean isApproved =false;

    private boolean isRejected =false;

    private String clientEmail;

    private String status;

    private Integer version;

    private String created_by;

    private String update_by;

    private Date createdAt;

    private Date updatedAt;

    public String getUpdate_by() {
        if (update_by == null) {
            return update_by = "There is no update yet";
        }
        return update_by;
    }


}
