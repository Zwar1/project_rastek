package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectBugService {
    @Autowired
    private ProjectBugRepository projectBugRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ValidationService validationService;

    private final String uploadDir = "uploads/projects/bug";

    @Transactional
    public ProjectBugRes uploadFile(ProjectBugReq req) {
        validationService.validate(req);

        ProjectEntity projectEntity = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));

        ProjectBugEntity projectBug = new ProjectBugEntity();

        try {
            // Create file directory if it doesn't exist
            String filePath = uploadDir + "/" + projectEntity.getId();
            Path path = Paths.get(filePath);
            Files.createDirectories(path);

            // Generate unique file name, change to name and uploader's ID (employee's NIK) later if needed
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(req.getFile().getOriginalFilename()));
            String fileName = System.currentTimeMillis() + "_" + originalFileName;

            Path targetLocation = path.resolve(fileName);
            Files.copy(req.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            projectBug.setFileName(fileName);
            projectBug.setFilePath(filePath + "/" + fileName);
            projectBug.setTitle(req.getTitle());
            projectBug.setDescription(req.getDescription());
            projectBug.setUploadedOn(LocalDateTime.now());
            projectBug.setProject(projectEntity);

            projectBug = projectBugRepository.save(projectBug);

            return mapToResponse(projectBug);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not store file. Please try again!", e);
        }
    }

    @Transactional
    public ProjectBugRes getBugReport(Long id) {
        ProjectBugEntity bugEntity = projectBugRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug report entry not found"));
        return mapToResponse(bugEntity);
    }

    @Transactional
    public List<ProjectBugRes> getAllBugsByProjectId(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return projectBugRepository.findByProjectId(projectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProjectBugRes> getAllBugs() {
        List<ProjectBugEntity> tasks = projectBugRepository.findAll();
        return tasks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectBugRes updateBugReport(Long id, ProjectBugReq req) {
        ProjectBugEntity bugEntity = projectBugRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug report entry not found"));

        if (Objects.nonNull(req.getTitle()) && !req.getTitle().isEmpty()) {
            bugEntity.setTitle(req.getTitle());
            projectBugRepository.save(bugEntity);
        }
        if (Objects.nonNull(req.getDescription()) && !req.getDescription().isEmpty()) {
            bugEntity.setDescription(req.getDescription());
            projectBugRepository.save(bugEntity);
        }
        if (req.getFile() != null && !req.getFile().isEmpty()) {
            try {
                // Remove old file if exists
                Path oldFilePath = Paths.get(bugEntity.getFilePath());
                Files.deleteIfExists(oldFilePath);

                // Save new file
                String filePath = uploadDir + "/" + bugEntity.getProject().getId();
                Path path = Paths.get(filePath);
                Files.createDirectories(path);

                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(req.getFile().getOriginalFilename()));
                String fileName = System.currentTimeMillis() + "_" + originalFileName;
                Path targetLocation = path.resolve(fileName);
                Files.copy(req.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                bugEntity.setFileName(fileName);
                bugEntity.setFilePath(filePath + "/" + fileName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Could not update file. Please try again!", e);
            }
        }
        projectBugRepository.save(bugEntity);

        return mapToResponse(bugEntity);
    }

    @Transactional
    public void deleteBugReport(Long projectId, Long bugReportId) {
        ProjectBugEntity bug = projectBugRepository.findByIdAndProjectId(bugReportId, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug entry report not found"));

        try {
            Path filePath = Paths.get(bug.getFilePath());
            Files.deleteIfExists(filePath);

            projectBugRepository.delete(bug);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not delete the bug entry report. Please try again!", e);
        }
    }

    private ProjectBugRes mapToResponse(ProjectBugEntity bug){
        return ProjectBugRes.builder()
                .id(bug.getId())
                .project_id(bug.getProject().getId())
                .fileName(bug.getFileName())
                .filePath(bug.getFilePath())
                .title(bug.getTitle())
                .description(bug.getDescription())
                .uploadedOn(bug.getUploadedOn())
                .build();
    }
}