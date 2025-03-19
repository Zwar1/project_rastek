package com.example.userCrud.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LeaveRequestRes {

    private Long id;

    private String nama;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> jenis;

    private String alasan;

    private String status;

    private List<LeaveApprovalRes> leaveApprovalRes;
}
