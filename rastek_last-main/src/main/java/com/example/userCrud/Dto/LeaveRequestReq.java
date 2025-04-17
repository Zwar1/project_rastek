package com.example.userCrud.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class LeaveRequestReq {

    private Long NIK;

    private LocalDate startDate;

    private LocalDate endDate;

    private String alasan;

    private String status = "";

    private Long idLeave;

}
