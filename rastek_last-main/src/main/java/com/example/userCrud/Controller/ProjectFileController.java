package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ProjectFileReq;
import com.example.userCrud.Dto.ProjectFileRes;
import com.example.userCrud.Entity.ProjectFile;
import com.example.userCrud.Service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProjectFileController {
    private static final String CLIENTS_FILES_VIEW = "CLIENTS:FILES:VIEW";
    private static final String CLIENTS_FILES_ADD = "CLIENTS:FILES:UPLOAD";
    private static final String CLIENTS_FILES_DELETE = "CLIENTS:FILES:DELETE";

    @Autowired
    private ProjectFileService projectFileService;

    @PreAuthorize("hasAuthority('" + CLIENTS_FILES_ADD + "')")
    @PostMapping(
            path = "/api/project/{id}/files/add"
    )
    public ProjectFileRes uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploaderId") Long uploaderId,
            @RequestParam("uploaderType") ProjectFile.UploaderType uploaderType) {
        ProjectFileReq req = ProjectFileReq.builder()
                .projectId(id)
                .uploaderId(uploaderId)
                .uploaderType(uploaderType)
                .file(file)
                .build();

        return projectFileService.uploadFile(req);
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_FILES_VIEW + "')")
    @GetMapping(
            path = "/api/project/{id}/files"
    )
    public List<ProjectFileRes> getAllFilesByProjectId(@PathVariable Long id) {
        return projectFileService.getAllFilesByProjectId(id);
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_FILES_DELETE + "')")
    @DeleteMapping("/api/project/{id}/files/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id,
            @PathVariable Long fileId) {
        projectFileService.deleteFile(id, fileId);
        return ResponseEntity.ok("File deleted successfully");
    }
}
