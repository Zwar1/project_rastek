package com.example.userCrud.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class CVRes {
    private Long id;
    private String projectName;
    private String projectRole;
    private LocalDate projectStart;
    private LocalDate projectEnd;
    private String projectDescription;
}
