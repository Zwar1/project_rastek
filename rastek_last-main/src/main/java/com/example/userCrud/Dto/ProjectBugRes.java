package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBugRes {
    private Long id;
    private Long project_id;
    private String fileName;
    private String filePath;
    private String title;
    private String description;
    private LocalDateTime uploadedOn;
}