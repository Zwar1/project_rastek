package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.Roles;
import com.example.userCrud.Service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.userCrud.Service.RoleService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping(path = "/addRoles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public web_response<RoleResponse> createRole(
            @RequestBody RoleCreateReq request) {
        RoleResponse roleResponse = roleService.CreateRole(request.getRoleReq(), request.getPermissionIds());
        return web_response.<RoleResponse>builder()
                .data(roleResponse)
                .message("Success")
                .build();
    }

    @GetMapping("/get/{roleId}")
    public web_response<RoleResponse> getRole(
            @PathVariable("roleId") Long roleId) {
        RoleResponse roleResponse = roleService.GetRole(roleId);
        return web_response.<RoleResponse>builder()
                .data(roleResponse)
                .message("Success")
                .build();
    }

    @GetMapping("/getAllRoles")
    public web_response<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRole();
        return web_response.<List<RoleResponse>>builder()
                .data(roles)
                .message("Success")
                .build();
    }

    @PutMapping("/update/{roleId}")
    public web_response<RoleResponse> updateRole(
            @PathVariable("roleId") Long roleId,
            @RequestBody RoleReq request) {
        RoleResponse roleResponse = roleService.UpdateRole(roleId, request);
        return web_response.<RoleResponse>builder()
                .data(roleResponse)
                .message("Success")
                .build();
    }

    @DeleteMapping("/delete/{roleId}")
    public web_response<String> deleteRole(
            @PathVariable("roleId") Long roleId) {
        roleService.DeleteRole(roleId);
        return web_response.<String>builder()
                .message("Success")
                .build();
    }


    @GetMapping("/roles/{roleId}/permissions")
    public List<RolePermissionRes> getPermissionsForRole(@PathVariable Long roleId) {
        return rolePermissionService.getPermissionsForRole(roleId);
    }
}