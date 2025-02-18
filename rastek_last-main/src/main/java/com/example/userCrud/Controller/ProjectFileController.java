package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ProjectFileReq;
import com.example.userCrud.Dto.ProjectFileRes;
import com.example.userCrud.Entity.ProjectFile;
import com.example.userCrud.Service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProjectFileController {
    @Autowired
    private ProjectFileService projectFileService;

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

    @GetMapping(
            path = "/api/project/{id}/files"
    )
    public List<ProjectFileRes> getAllFilesByProjectId(@PathVariable Long id) {
        return projectFileService.getAllFilesByProjectId(id);
    }

    @DeleteMapping("/api/project/{id}/files/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id,
            @PathVariable Long fileId) {
        projectFileService.deleteFile(id, fileId);
        return ResponseEntity.ok("File deleted successfully");
    }
}
