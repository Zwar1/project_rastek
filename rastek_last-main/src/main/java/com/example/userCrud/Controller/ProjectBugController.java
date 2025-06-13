package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ProjectBugReq;
import com.example.userCrud.Dto.ProjectBugRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.ProjectBugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProjectBugController {
    private static final String CLIENTS_BUGS_VIEW = "CLIENTS:BUG REPORT:VIEW";
    private static final String CLIENTS_BUGS_ADD = "CLIENTS:BUG REPORT:ADD";
    private static final String CLIENTS_BUGS_EDIT = "CLIENTS:BUG REPORT:EDIT";

    @Autowired
    private ProjectBugService projectBugService;

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_ADD + "')")
    @PostMapping(
            path = "/api/project/{projectId}/bugReport/add"
    )
    public ProjectBugRes uploadFile(
            @PathVariable("projectId") Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
    ) {
        ProjectBugReq req = ProjectBugReq.builder()
                .projectId(id)
                .file(file)
                .title(title)
                .description(description)
                .build();

        return projectBugService.uploadFile(req);
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_VIEW + "')")
    @GetMapping(
            path = "/api/project/bugReport/{bugReportId}"
    )
    public web_response<ProjectBugRes> getDetailedBugReport(@PathVariable("bugReportId") Long id) {
        ProjectBugRes response = projectBugService.getBugReport(id);
        return web_response.<ProjectBugRes>builder().data(response).build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_VIEW + "')")
    @GetMapping(
            path = "/api/project/{projectId}/bugReport"
    )
    public List<ProjectBugRes> getAllBugReportsByProjectId(@PathVariable("projectId") Long id) {
        return projectBugService.getAllBugsByProjectId(id);
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_VIEW + "')")
    @GetMapping(
            path = "/api/project/getAllBugReport"
    )
    public List<ProjectBugRes> getAllBugReports() {
        return projectBugService.getAllBugs();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_EDIT + "')")
    @PutMapping("/api/project/bugReport/{bugReportId}")
    public ProjectBugRes updateBugReport(
            @PathVariable("bugReportId") Long bugReportId,
            @ModelAttribute ProjectBugReq req) {
        return projectBugService.updateBugReport(bugReportId, req);
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_BUGS_EDIT + "')")
    @DeleteMapping("/api/project/{projectId}/bugReport/{bugReportId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable("projectId") Long id,
            @PathVariable("bugReportId") Long bugReportId) {
        projectBugService.deleteBugReport(id, bugReportId);
        return ResponseEntity.ok("File deleted successfully");
    }
}