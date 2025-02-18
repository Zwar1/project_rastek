package com.example.userCrud.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskRes {
    private Long id;
    private String title;
    private String description;

    @Min(0)
    @Max(100)
    private Integer progress;

    private LocalDate startDate;
    private LocalDate endDate;
    private List<ProjectTaskMemberRes> members;
    private ProjectRes project;
}
