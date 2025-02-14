package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ClientRes;
import com.example.userCrud.Dto.ProjectReq;
import com.example.userCrud.Dto.ProjectRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Entity.ProjectEntity;
import com.example.userCrud.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/api/create/project")
    public web_response<ProjectRes> createProject(@RequestBody ProjectReq request) {
        ProjectRes projectRes = projectService.createProject(request);
        return web_response.<ProjectRes>builder()
                .data(projectRes)
                .message("Project successfully created")
                .build();
    }

    @GetMapping(
            path = "/api/get/project/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ProjectRes> getProject(@PathVariable Long id) {
        ProjectRes projectRes = projectService.getProject(id);
        return web_response.<ProjectRes>builder()
                .data(projectRes)
                .message("Project successfully fetched")
                .build();
    }

    @GetMapping(
            path = "/api/get/project",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ProjectRes> getAll() {
        return projectService.getAllProject();
    }

    @GetMapping("/api/project/{id}/logo")
    public ResponseEntity<byte[]> getProjectLogo(@PathVariable Long id) {
        ProjectEntity project = projectService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getLogo() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(project.getLogoType()))
                .body(project.getLogo());
    }

    @DeleteMapping("/api/project/{id}/delete/logo")
    public web_response<String> deleteProjectLogo(@PathVariable Long id) {
        projectService.deleteProjectLogo(id);
        return web_response.<String>builder()
                .data(null)
                .message("Project logo successfully deleted")
                .build();
    }

    @PutMapping(
            path = "/api/update/project/{id}"
    )
    public web_response<ProjectRes> updateProject(@PathVariable Long id, @RequestBody ProjectReq request) {
        ProjectRes projectRes = projectService.updateProject(id, request);
        return web_response.<ProjectRes>builder()
                .data(projectRes)
                .message("Project successfully updated")
                .build();
    }

    @DeleteMapping("/api/delete/project/{id}")
    public web_response<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return web_response.<Void>builder()
                .message("Project successfully deleted")
                .build();
    }
}
