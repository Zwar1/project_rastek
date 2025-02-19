package com.example.userCrud.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReq {
    @NotBlank
    private String projectName;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String projectDescription;
    private String summary;
    private String priority;
    private String status;

    @Min(0)
    private Integer estimatedHours;
    private Integer totalHours;

    @Min(0)
    @Max(100)
    private Integer progress = 0;

    @NotNull
    private Long clientId;

    private List<Long> member;
//    private String logo;
//    private String logoType;
}
