package com.example.userCrud.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyEventReq {

    private String eventName;

    private Boolean isFree;

}
