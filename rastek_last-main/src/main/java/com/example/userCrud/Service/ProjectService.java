package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.ProjectEntity;
import com.example.userCrud.Repository.ClientRepository;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.ProjectRepository;
import org.simpleframework.xml.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ProjectRes createProject(ProjectReq req) {
        validationService.validate(req);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        ClientEntity client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        ProjectEntity project = new ProjectEntity();
        project.setProjectName(req.getProjectName());
        project.setProjectDescription(req.getProjectDescription());
        project.setProjectStart(req.getStartDate());
        project.setProjectEnd(req.getEndDate());
        project.setSummary(req.getSummary());
        project.setEstimatedHours(req.getEstimatedHours());
        project.setTotalHours(req.getTotalHours());
        project.setPriority(req.getPriority());
        project.setStatus(req.getStatus());
        project.setProgress(0);
        project.setClient(client);

        if (req.getMember() != null && !req.getMember().isEmpty()) {
            Set<EmployeeEntity> teamMembers = employeeRepository.findAllById(req.getMember())
                    .stream()
                    .collect(Collectors.toSet());
            project.setMember(teamMembers);
        }

        // Handle logo only if both logo and logoType are present
//        if (StringUtils.hasText(req.getLogo()) && StringUtils.hasText(req.getLogoType())) {
//            try {
//                byte[] logoData = Base64.getDecoder().decode(req.getLogo());
//                project.setLogo(logoData);
//                project.setLogoType(req.getLogoType());
//            } catch (IllegalArgumentException e) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid logo format");
//            }
//        } else {
//            // Explicitly set null when no logo is provided
//            project.setLogo(null);
//            project.setLogoType(null);
//        }

        projectRepository.save(project);

//        client.setIsActive(true);
//        clientRepository.save(client);

        return toProjectResponse(project);
    }

    private ProjectRes toProjectResponse(ProjectEntity project) {
        return ProjectRes.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .startDate(project.getProjectStart())
                .endDate(project.getProjectEnd())
                .summary(project.getSummary())
                .estimatedHours(project.getEstimatedHours())
                .totalHours(project.getTotalHours())
                .priority(project.getPriority())
                .status(project.getStatus())
                .progress(project.getProgress())
                .client(ProjectClientRes.builder()
                        .id(project.getClient().getId())
                        .name(project.getClient().getClientName())
                        .clientAddress(project.getClient().getClientAddress())
                        .clientCountry(project.getClient().getClientCountry())
                        .profilePicture(project.getClient().getProfilePicture())
                        .profilePictureType(project.getClient().getProfilePictureType())
                        .build())
                // Map team members - only include essential employee info
                .member(project.getMember() == null ? null :
                        project.getMember().stream()
                                .map(employee -> ProjectEmployeeRes.builder()
                                        .NIK(employee.getNIK())
                                        .name(employee.getName())  // Only include name
                                        .build())
                                .collect(Collectors.toList()))
                // .logo((project.getLogo() != null && project.getLogoType() != null) ?
                //         project.getLogoType() + "," + Base64.getEncoder().encodeToString(project.getLogo()) : null)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public ProjectRes getProject(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return toProjectResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectRes> getAllProject() {
        List<ProjectEntity> projects = projectRepository.findAll();
        return projects.stream().map(this::toProjectResponse).collect(Collectors.toList());
    }

    // Sorted fetch by project end date ASC order
    @Transactional(readOnly = true)
    public List<ProjectRes> getAllProjectSortedBy(String sortBy, String order) {
        List<ProjectEntity> projects = List.of();
        order = order.toLowerCase().trim();
        sortBy = sortBy.toLowerCase().trim();

        // Add more sort by parameters
        switch (sortBy) {
            case "enddate":
                projects = order.equals("asc") ?
                        projectRepository.findAllByOrderByProjectEndAsc() :
                        projectRepository.findAllByOrderByProjectEndDesc();
                break;
            case "priority":
                projects = order.equals("asc") ?
                        projectRepository.findAllByOrderByPriorityAsc() :
                        projectRepository.findAllByOrderByPriorityDesc();
                break;
            default:
                projects = projectRepository.findAllByOrderByProjectEndAsc();
        }
        return projects.stream().map(this::toProjectResponse).collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public byte[] getProjectLogo(Long id) {
//        ProjectEntity project = projectRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
//
//        if (project.getLogo() == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logo not found");
//        }
//
//        return project.getLogo();
//    }

    @Transactional(readOnly = true)
    public Optional<ProjectEntity> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public ProjectRes updateProject(Long id, ProjectReq req) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        ProjectReq validationReq = ProjectReq.builder()
                .projectName(StringUtils.hasText(req.getProjectName()) ? req.getProjectName() : project.getProjectName())
                .startDate(req.getStartDate() != null ? req.getStartDate() : project.getProjectStart())
                .endDate(req.getEndDate() != null ? req.getEndDate() : project.getProjectEnd())
                .projectDescription(req.getProjectDescription())
                .summary(req.getSummary())
                .priority(req.getPriority())
                .status(req.getStatus() != null ? req.getStatus() : project.getStatus())
                .progress(req.getProgress() != null ? req.getProgress() : project.getProgress())
                .estimatedHours(req.getEstimatedHours())
                .totalHours(req.getTotalHours())
                .clientId(req.getClientId() != null ? req.getClientId() : project.getClient().getId())
                .member(req.getMember())
                //.logo(req.getLogo())
                //.logoType(req.getLogoType())
                .build();

        validationService.validate(validationReq);

        if (req.getProjectName() != null) {
            project.setProjectName(req.getProjectName());
            projectRepository.save(project);
        }
        if (req.getProjectDescription() != null) {
            project.setProjectDescription(req.getProjectDescription());
            projectRepository.save(project);
        }
        if (req.getStartDate() != null) {
            project.setProjectStart(req.getStartDate());
            projectRepository.save(project);
        }
        if (req.getEndDate() != null) {
            project.setProjectEnd(req.getEndDate());
            projectRepository.save(project);
        }
        if (req.getSummary() != null) {
            project.setSummary(req.getSummary());
            projectRepository.save(project);
        }
        if (req.getPriority() != null) {
            project.setPriority(req.getPriority());
            projectRepository.save(project);
        }
        if (req.getStatus() != null) {
            project.setStatus(req.getStatus());
            projectRepository.save(project);
        }
        if (req.getEstimatedHours() != null) {
            project.setEstimatedHours(req.getEstimatedHours());
            projectRepository.save(project);
        }
        if (req.getTotalHours() != null) {
            project.setTotalHours(req.getTotalHours());
            projectRepository.save(project);
        }
        if (req.getProgress() != null) {
            project.setProgress(req.getProgress());
            projectRepository.save(project);
        }
        if (req.getMember() != null) {
            Set<EmployeeEntity> teamMembers = employeeRepository.findAllById(req.getMember())
                    .stream()
                    .collect(Collectors.toSet());
            project.setMember(teamMembers);
            projectRepository.save(project);
        }
//        if (StringUtils.hasText(req.getLogo()) && StringUtils.hasText(req.getLogoType())) {
//            updateProjectLogo(project, req.getLogo(), req.getLogoType());
//        }
        projectRepository.save(project);
        return toProjectResponse(project);
    }

    @Transactional
    public ProjectRes updateProjectStatus(Long id, ChangeProjectStatusReq req) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

//        ProjectReq validationReq = ProjectReq.builder()
//                .status(req.getStatus() != null ? req.getStatus() : project.getStatus())
//                .build();
//
//        validationService.validate(validationReq);

        if (req.getStatus() != null) {
            project.setStatus(req.getStatus());
            projectRepository.save(project);
        }

        return toProjectResponse(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        // Remove member references
        if (project.getMember() != null) {
            project.getMember().clear();
        }

        projectRepository.delete(project);
    }

//    @Transactional
//    public void updateProjectLogo(ProjectEntity project, String logoBase64, String logoType) {
//        try {
//            byte[] logoData = Base64.getDecoder().decode(logoBase64);
//            project.setLogo(logoData);
//            project.setLogoType(logoType);
//            projectRepository.save(project);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid logo format");
//        }
//    }

//    @Transactional
//    public void deleteProjectLogo(Long id) {
//        ProjectEntity project = projectRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
//
//        if (project.getLogo() == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project has no logo to delete");
//        }
//
//        project.setLogo(null);
//        project.setLogoType(null);
//        projectRepository.save(project);
//    }
}
