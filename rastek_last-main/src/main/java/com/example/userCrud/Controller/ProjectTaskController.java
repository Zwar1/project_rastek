package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectTaskController {
    private static final String CLIENTS_TASK_VIEW = "CLIENTS:TASK:VIEW";
    private static final String CLIENTS_TASK_ADD = "CLIENTS:TASK:ADD";
    private static final String CLIENTS_TASK_DETAILS_VIEW = "CLIENTS:TASK DETAILS:VIEW";
    private static final String CLIENTS_TASK_DETAILS_EDIT = "CLIENTS:TASK DETAILS:EDIT";
   // private static final String CLIENTS_TASK_STATUS_EDIT = "CLIENTS:TASK STATUS:EDIT";

    @Autowired
    private ProjectTaskService projectTaskService;

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_ADD + "')")
    @PostMapping(
            path = "/api/project/{projectId}/addTask",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ProjectTaskRes> addProjectTask(
            @PathVariable("projectId") Long projectId,
            @RequestBody ProjectTaskReq request) {
        ProjectTaskRes addTaskRes = projectTaskService.createTask(projectId, request);
        return web_response.<ProjectTaskRes>builder()
                .data(addTaskRes)
                .message("Task successfully created")
                .build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_DETAILS_VIEW + "')")
    @GetMapping(
            path = "/api/project/getTask/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ProjectTaskRes> getByEmployee(@PathVariable("taskId") Long taskId) {
        ProjectTaskRes response = projectTaskService.getTask(taskId);
        return web_response.<ProjectTaskRes>builder().data(response).build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_VIEW + "')")
    @GetMapping(
            path = "/api/project/getAllTasks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<List<ProjectTaskRes>> getAllTasks() {
        List<ProjectTaskRes> response = projectTaskService.getAllTask();
        return web_response.<List<ProjectTaskRes>>builder().data(response).build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_DETAILS_VIEW + "')")
    @GetMapping(
            path = "/api/project/{projectId}/getAllTasks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<List<ProjectTaskRes>> getAllProjectTask(@PathVariable("projectId") Long projectId) {
        List<ProjectTaskRes> response = projectTaskService.getAllProjectTask(projectId);
        return web_response.<List<ProjectTaskRes>>builder().data(response).build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_DETAILS_EDIT + "')")
    @PutMapping(
            path = "/api/project/updateTask/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ProjectTaskRes> updateTask(@PathVariable("taskId") Long taskId, @RequestBody ProjectTaskReq request) {
        ProjectTaskRes updateTaskRes = projectTaskService.updateTask(taskId, request);
        return web_response.<ProjectTaskRes>builder().data(updateTaskRes).build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_TASK_DETAILS_EDIT + "')")
    @DeleteMapping(
            path = "/api/project/{projectId}/deleteTask/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<Void> deleteTask(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        projectTaskService.deleteTask(projectId, taskId);
        return web_response.<Void>builder().build();
    }
}
