package com.example.userCrud.Controller;

import com.example.userCrud.Dto.PermissionReq;
import com.example.userCrud.Dto.PermissionRes;
import com.example.userCrud.Dto.ProjectRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionsController {
    @Autowired
    private PermissionService permissionService;

//    @PostMapping
//    public web_response<PermissionRes> createPermission(@RequestBody PermissionReq req) {
//        PermissionRes PermissionRes = permissionService.createPermission(req);
//        return web_response.<PermissionRes>builder()
//                .data(PermissionRes)
//                .message("Permission successfully created")
//                .build();
//    }

//    @PutMapping
//    public web_response<PermissionRes editPermission

    @GetMapping
    public List<PermissionRes> getAllPermission(@RequestBody PermissionReq req) {
        List<PermissionRes> permissionRes = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissionRes).getBody();
    }
}
