package com.example.userCrud.Dto;

import lombok.*;


@Getter
@Setter@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLeaveReq {

    private String namaCuti;

    private Integer jatahawal;
}
