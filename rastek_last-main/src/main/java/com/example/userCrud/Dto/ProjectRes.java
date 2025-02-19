package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRes {
    private Long id;
    private String projectName;
    private String projectDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private String summary;
    private String status;
    private String priority;
    private ProjectClientRes client;
    private List<ProjectEmployeeRes> member;
    private Integer estimatedHours;
    private Integer totalHours;
    private Integer progress;
//    private String logo;
    private Date createdAt;
    private Date updatedAt;

}

