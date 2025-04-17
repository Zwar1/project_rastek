package com.example.userCrud.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectTaskReq {
    @NotBlank
    private String title;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
    private String description;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer progress;

    private boolean onHold;

    @NotNull
    private List<Long> taskMember;

//    @NotNull
//    private Long projectId;
}
