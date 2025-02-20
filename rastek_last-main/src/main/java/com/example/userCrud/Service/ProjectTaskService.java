package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.ProjectEntity;
import com.example.userCrud.Entity.ProjectTask;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.ProjectRepository;
import com.example.userCrud.Repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectTaskService {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ProjectTaskRes createTask(Long projectId, ProjectTaskReq req) {
        validationService.validate(req);

        ProjectTask projectTask = new ProjectTask();
        projectTask.setTitle(req.getTitle());
        projectTask.setDescription(req.getDescription());
        projectTask.setStartDate(req.getStartDate());
        projectTask.setEndDate(req.getEndDate());
        projectTask.setProgress(0);

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        projectTask.setProject(project);

        Set<EmployeeEntity> members = new HashSet<>();
        for (Long memberId : req.getTaskMember()) {
            EmployeeEntity employee = employeeRepository.findById(memberId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Employee not found with ID: " + memberId));
            members.add(employee);
        }
        projectTask.setTaskMember(members);

        projectTask = projectTaskRepository.save(projectTask);

        return mapToResponse(projectTask);
    }

    private ProjectTaskRes mapToResponse(ProjectTask task) {
        return ProjectTaskRes.builder()
                .id(task.getIdProjectTask())
                .title(task.getTitle())
                .description(task.getDescription())
                .progress(task.getProgress())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .members(task.getTaskMember().stream()
                        .map(this::mapMemberToResponse)
                        .collect(Collectors.toList()))
                .project(mapProjectToResponse(task.getProject()))
                .build();
    }

    private ProjectTaskMemberRes mapMemberToResponse(EmployeeEntity employee) {
        return ProjectTaskMemberRes.builder()
                .id(employee.getNIK())
                .name(employee.getName())
                .build();
    }

    private ProjectRes mapProjectToResponse(ProjectEntity project) {
        return ProjectRes.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .startDate(project.getProjectStart())
                .endDate(project.getProjectEnd())
                .summary(project.getSummary())
                .estimatedHours(project.getEstimatedHours())
                .totalHours(project.getTotalHours())
                .client(ProjectClientRes.builder()
                        .id(project.getClient().getId())
                        .name(project.getClient().getClientName())
                        .build())
                // Map team members - only include essential employee info
                .member(project.getMember() == null ? null :
                        project.getMember().stream()
                                .map(employee -> ProjectEmployeeRes.builder()
                                        .NIK(employee.getNIK())
                                        .name(employee.getName())  // Only include name
                                        .build())
                                .collect(Collectors.toList()))
                //.logo((project.getLogo() != null && project.getLogoType() != null) ? project.getLogoType() + "," + Base64.getEncoder().encodeToString(project.getLogo()) : null)
                .build();
    }

    @Transactional
    public ProjectTaskRes getTask(Long id) {
        ProjectTask task = projectTaskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        return mapToResponse(task);
    }

    @Transactional
    public List<ProjectTaskRes> getAllTask() {
        List<ProjectTask> tasks = projectTaskRepository.findAll();
        return tasks.stream()
               .map(this::mapToResponse)
               .collect(Collectors.toList());
    }

    @Transactional
    public ProjectTaskRes updateTask(Long id, ProjectTaskReq req) {
        ProjectTask taskEntity = projectTaskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (Objects.nonNull(req.getTitle()) && !req.getTitle().isEmpty()) {
            taskEntity.setTitle(req.getTitle());
            projectTaskRepository.save(taskEntity);
        }
        if (Objects.nonNull(req.getDescription()) && !req.getDescription().isEmpty()) {
            taskEntity.setDescription(req.getDescription());
            projectTaskRepository.save(taskEntity);
        }
        if (Objects.nonNull(req.getStartDate())) {
            taskEntity.setStartDate(req.getStartDate());
            projectTaskRepository.save(taskEntity);
        }
        if (Objects.nonNull(req.getEndDate())) {
            taskEntity.setStartDate(req.getEndDate());
            projectTaskRepository.save(taskEntity);
        }
        if (Objects.nonNull(req.getTitle()) && !req.getTitle().isEmpty()) {
            taskEntity.setTitle(req.getTitle());
            projectTaskRepository.save(taskEntity);
        }
        if (Objects.nonNull(req.getProgress())) {
            taskEntity.setProgress(req.getProgress());
            projectTaskRepository.save(taskEntity);
        }
        if (req.getTaskMember() != null) {
            Set<EmployeeEntity> teamMembers = employeeRepository.findAllById(req.getTaskMember())
                    .stream()
                    .collect(Collectors.toSet());
            taskEntity.setTaskMember(teamMembers);
            projectTaskRepository.save(taskEntity);
        }
        projectTaskRepository.save(taskEntity);
        return mapToResponse(taskEntity);
    }

    @Transactional
    public void deleteTask(Long projectId, Long taskId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Project not found with ID: " + projectId));

        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task not found with ID: " + taskId));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Task " + taskId + " does not belong to project " + projectId);
        }

        task.getTaskMember().clear();
        projectTaskRepository.save(task);

        projectTaskRepository.delete(task);
    }
}
