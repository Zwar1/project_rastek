package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LeaveApprovalRes {

    private Long id;

    private Long idRequest;

    private String namaApproval;

    private String status;

}
