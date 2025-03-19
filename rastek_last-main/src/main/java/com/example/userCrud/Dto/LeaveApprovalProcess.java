package com.example.userCrud.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LeaveApprovalProcess {

    private Long requestId;

    private Long nikApprover;

    private String status;
}
