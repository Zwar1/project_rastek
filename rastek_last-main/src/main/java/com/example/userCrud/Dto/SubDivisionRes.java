package com.example.userCrud.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubDivisionRes {

    private Long id;

    private String subDivision_name;

    private Long divisionId;

    private String divisionName;
}
