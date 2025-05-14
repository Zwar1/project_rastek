package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ChangeProjectStatusReq;
import com.example.userCrud.Dto.ProjectReq;
import com.example.userCrud.Dto.ProjectRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private static final String CLIENTS_PROJECT_VIEW = "CLIENTS:PROJECT:VIEW";
    private static final String CLIENTS_PROJECT_ADD = "CLIENTS:PROJECT:ADD";
    private static final String CLIENTS_PROJECT_DETAILS_VIEW = "CLIENTS:PROJECT DETAILS:VIEW";
    private static final String CLIENTS_PROJECT_DETAILS_EDIT = "CLIENTS:PROJECT DETAILS:EDIT";
    private static final String CLIENTS_PROJECT_STATUS_EDIT = "CLIENTS:PROJECT STATUS:EDIT";

    @Autowired
    ProjectService projectService;

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_ADD + "')")
    @PostMapping("/api/create/project")
    public web_response<ProjectRes> createProject(
            @RequestBody ProjectReq request
    ) {
        ProjectRes projectRes = projectService.createProject(request);
        return web_response.<ProjectRes>builder()
                .data(projectRes)
                .message("Project successfully created")
                .build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_DETAILS_VIEW + "')")
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

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_VIEW + "')")
    @GetMapping(
            path = "/api/get/project",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ProjectRes> getAll(
            @RequestParam(defaultValue = " endDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<ProjectRes> sortedProjects = projectService.getAllProjectSortedBy(sortBy, sortDir);
        return ResponseEntity.ok(sortedProjects).getBody();
    }

//    @GetMapping("/api/project/{id}/logo")
//    public ResponseEntity<byte[]> getProjectLogo(@PathVariable Long id) {
//        ProjectEntity project = projectService.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
//
//        if (project.getLogo() == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(project.getLogoType()))
//                .body(project.getLogo());
//    }

//    @DeleteMapping("/api/project/{id}/delete/logo")
//    public web_response<String> deleteProjectLogo(@PathVariable Long id) {
//        projectService.deleteProjectLogo(id);
//        return web_response.<String>builder()
//                .data(null)
//                .message("Project logo successfully deleted")
//                .build();
//    }

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_DETAILS_EDIT + "')")
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

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_STATUS_EDIT + "')")
    @PutMapping(
            path = "/api/update/project/{id}/status"
    )
    public web_response<ProjectRes> updateProjectStatus(@PathVariable Long id, @RequestBody ChangeProjectStatusReq request) {
        ProjectRes projectRes = projectService.updateProjectStatus(id, request);
        return web_response.<ProjectRes>builder()
                .data(projectRes)
                .message("Project's Status successfully updated")
                .build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_PROJECT_DETAILS_EDIT + "')")
    @DeleteMapping("/api/delete/project/{id}")
    public web_response<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return web_response.<Void>builder()
                .message("Project successfully deleted")
                .build();
    }
}
