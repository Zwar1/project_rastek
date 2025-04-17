package com.example.userCrud.Dto;

import com.example.userCrud.Entity.ProjectFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileRes {
    private Long id;
    private Long project_id;
    private Long uploader_id;
    private String fileName;
    private String uploaderName;
    private String filePath;
    private ProjectFile.UploaderType uploaderType;
    private LocalDateTime uploadedOn;
}
