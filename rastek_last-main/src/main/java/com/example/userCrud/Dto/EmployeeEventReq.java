package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEventReq {
    private String eventName;

    private Boolean isCuti;

    private Long NIK;
}
