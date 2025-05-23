package com.example.userCrud.Dto;

import com.example.userCrud.Entity.ProjectFile;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProjectBugReq {
    @NotNull
    private Long projectId;

    private String title;
    private String description;
    private MultipartFile file;
}
