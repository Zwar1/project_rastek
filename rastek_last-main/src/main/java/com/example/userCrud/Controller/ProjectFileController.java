package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ProjectFileReq;
import com.example.userCrud.Dto.ProjectFileRes;
import com.example.userCrud.Entity.ProjectFile;
import com.example.userCrud.Service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ProjectFileController {
    private static final String CLIENTS_FILES_VIEW = "CLIENTS:FILES:VIEW";
    private static final String CLIENTS_FILES_ADD = "CLIENTS:FILES:UPLOAD";
    private static final String CLIENTS_FILES_EDIT = "CLIENTS:FILES:EDIT";

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

    @PreAuthorize("hasAuthority('" + CLIENTS_FILES_VIEW + "')")
    @GetMapping(
            path = "/api/project/{projectId}/files/{fileId}/download"
    )
    public ResponseEntity<Resource> downloadFile(@PathVariable Long projectId, @PathVariable Long fileId) {
        ProjectFile file;
        String baseDir = null;
        Path path = null;
        try {
            file = projectFileService.getFileByIdAndProjectId(fileId, projectId);
            baseDir = "E:/Rastek_app/";
            path = Paths.get(baseDir).resolve(file.getFilePath()).normalize();
            System.out.println("base Dir" + baseDir + ", file path: " + path);
        } catch (ResponseStatusException ex) {
            // Override the message here
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "base Dir" + baseDir + ", file path: " + path);
        }
        try {
            System.out.println("base Dir" + baseDir + ", file path: " + path);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "base Dir" + baseDir + ", file path: " + path);
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not download file", e);
        }
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_FILES_EDIT + "')")
    @DeleteMapping("/api/project/{id}/files/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id,
            @PathVariable Long fileId) {
        projectFileService.deleteFile(id, fileId);
        return ResponseEntity.ok("File deleted successfully");
    }
}