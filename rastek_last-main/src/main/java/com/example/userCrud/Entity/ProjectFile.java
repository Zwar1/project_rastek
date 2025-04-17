package com.example.userCrud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_files")
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_file")
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "uploaded_on", nullable = false)
    private LocalDateTime uploadedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "uploader_type", nullable = false)
    private UploaderType uploaderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = true)
    private EmployeeEntity employeeUploader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = true)
    private ClientEntity clientUploader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    public enum UploaderType {
        EMPLOYEE,
        CLIENT
    }

    // Helper method to get uploader name and ID regardless of type
    @JsonIgnore
    public String getUploaderName() {
        return uploaderType == UploaderType.EMPLOYEE ?
                employeeUploader.getName() :
                clientUploader.getClientName();
    }

    @JsonIgnore
    public Long getUploaderId() {
        return uploaderType == UploaderType.EMPLOYEE ?
                employeeUploader.getNIK() :
                clientUploader.getId();
    }
}
