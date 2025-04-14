package com.example.userCrud.Service;

import com.example.userCrud.Dto.ProjectFileReq;
import com.example.userCrud.Dto.ProjectFileRes;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.ProjectEntity;
import com.example.userCrud.Entity.ProjectFile;
import com.example.userCrud.Repository.ClientRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.ProjectFileRepository;
import com.example.userCrud.Repository.ProjectRepository;
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
import java.util.stream.Collectors;

@Service
public class ProjectFileService {
    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ValidationService validationService;

    private final String uploadDir = "uploads/projects";

    @Transactional
    public ProjectFileRes uploadFile(ProjectFileReq req) {
        validationService.validate(req);

        ProjectEntity projectEntity = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Not Found"));

        ProjectFile projectFile = new ProjectFile();
        projectFile.setUploaderType(req.getUploaderType());

        if (req.getUploaderType() == ProjectFile.UploaderType.EMPLOYEE) {
            EmployeeEntity employee = employeeRepository.findFirstByNIK(req.getUploaderId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));
            projectFile.setEmployeeUploader(employee);
        } else {
            ClientEntity client = clientRepository.findById(req.getUploaderId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Not Found"));
            projectFile.setClientUploader(client);
        }

        try {
            // Create file directory if it doesn't exist
            String filePath = uploadDir + "/" + projectEntity.getId();
            Path path = Paths.get(filePath);
            Files.createDirectories(path);

            // Generate unique file name, change to name and uploader's ID (employee's NIK) later if needed
            String originalFileName = StringUtils.cleanPath(req.getFile().getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFileName;

            Path targetLocation = path.resolve(fileName);
            Files.copy(req.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            projectFile.setFileName(fileName);
            projectFile.setFilePath(filePath + "/" + fileName);
            projectFile.setUploadedOn(LocalDateTime.now());
            projectFile.setProject(projectEntity);

            projectFile = projectFileRepository.save(projectFile);

            return mapToResponse(projectFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not store file. Please try again!", e);
        }
    }

    @Transactional
    public List<ProjectFileRes> getAllFilesByProjectId(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return projectFileRepository.findByProjectId(projectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProjectFileRes mapToResponse(ProjectFile file){
        return ProjectFileRes.builder()
                .id(file.getId())
                .project_id(file.getProject().getId())
                .fileName(file.getFileName())
                .filePath(file.getFilePath())
                .uploaderName(file.getUploaderName())
                .uploaderType(file.getUploaderType())
                .uploader_id(file.getUploaderId())
                .uploadedOn(file.getUploadedOn())
                .build();
    }

    @Transactional
    public void deleteFile(Long projectId, Long fileId) {
        ProjectFile file = projectFileRepository.findByIdAndProjectId(fileId, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        try {
            Path filePath = Paths.get(file.getFilePath());
            Files.deleteIfExists(filePath);

            projectFileRepository.delete(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not delete file. Please try again!", e);
        }
    }
}
