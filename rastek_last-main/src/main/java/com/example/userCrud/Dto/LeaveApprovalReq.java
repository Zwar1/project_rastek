package com.example.userCrud.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LeaveApprovalReq {

    private Long idRequest;

    private List<Long> nikApproval;
}
