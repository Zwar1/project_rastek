package com.example.userCrud.Service;

import com.example.userCrud.Dto.RolePermissionReq;
import com.example.userCrud.Dto.RolePermissionRes;
import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Entity.RolePermissionEntity;
import com.example.userCrud.Entity.RolePermissionId;
import com.example.userCrud.Entity.Roles;
import com.example.userCrud.Repository.PermissionsRepository;
import com.example.userCrud.Repository.RolePermissionRepository;
import com.example.userCrud.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Permissions;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Transactional
    public RolePermissionRes assignPermissionToRole(RolePermissionReq request) {
        // Validate role exists
        Roles role = rolesRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        // Validate permission exists
        PermissionsEntity permission = permissionsRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found"));

        RolePermissionEntity rolePermission = new RolePermissionEntity();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermission.setAllowed(request.isAllowed());

        rolePermissionRepository.save(rolePermission);

        return toResponse(rolePermission);
    }

    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        RolePermissionEntity rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role-Permission assignment not found"));

        rolePermissionRepository.delete(rolePermission);
    }

    @Transactional(readOnly = true)
    public List<RolePermissionRes> getPermissionsForRole(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePermissionStatus(Long roleId, Long permissionId, boolean isAllowed) {
        RolePermissionEntity rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role-Permission assignment not found"));

        rolePermission.setAllowed(isAllowed);
        rolePermissionRepository.save(rolePermission);
    }

    @Transactional
    public void deleteAllRolePermissions(Long roleId) {
        rolePermissionRepository.deleteByRoleId(roleId);
    }

    private RolePermissionRes toResponse(RolePermissionEntity rolePermission) {
        return RolePermissionRes.builder()
                .rolesId(rolePermission.getRole().getId())
                .permissionsId(rolePermission.getPermission().getId())
                .permissionKey(rolePermission.getPermission().getPermissionKey())
                .isAllowed(rolePermission.isAllowed())
                .build();
    }
}