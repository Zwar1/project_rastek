package com.example.userCrud.Controller;

import com.example.userCrud.Entity.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.userCrud.Dto.CreateRoleRequest;
import com.example.userCrud.Dto.RoleResponse;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Service.RoleService;

import java.util.List;


@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public web_response<RoleResponse> createRole(@RequestBody CreateRoleRequest request) {
        //TODO: process POST request
        RoleResponse roleResponse = roleService.CreateRole(request);
        return web_response.<RoleResponse>builder().data(roleResponse).message("Success").build();
    }

    @GetMapping("/get/{roleId}")
    public web_response<RoleResponse> getRole(@PathVariable("roleId") Long roleId) {
        RoleResponse roleResponse = roleService.GetRole(roleId);
        return web_response.<RoleResponse>builder().data(roleResponse).message("Success").build();
    }

    @GetMapping("/get")
    public List<RoleResponse> GetAll(){
        return roleService.getAllRole();
    }

    @PutMapping("/update/{roleId}")
    public web_response<RoleResponse> updateRole(@PathVariable("roleId") Long roleId, @RequestBody CreateRoleRequest request) {
        RoleResponse roleResponse = roleService.UpdateRole(roleId,request);
        return web_response.<RoleResponse>builder().data(roleResponse).message("Success").build();
    }

    @DeleteMapping("delete/{roleId}")
    public web_response<String> deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.DeleteRole(roleId);
        return web_response.<String>builder().message("Success").build();
    }
}
