package com.example.userCrud.Service;

import com.example.userCrud.Dto.RoleReq;
import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Entity.RolePermissionEntity;
import com.example.userCrud.Repository.PermissionsRepository;
import com.example.userCrud.Repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.userCrud.Dto.RoleResponse;
import com.example.userCrud.Entity.Roles;
import com.example.userCrud.Repository.RolesRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public RoleResponse CreateRole(RoleReq request, Set<Long> permissionIds) {
        validationService.validate(request);

        if (rolesRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Roles role = new Roles();
        role.setName(request.getName());
        role.setStatus(request.getStatus());
        role.setCreatedAt(LocalDateTime.now());
        role.setCreated_by(currentUsername);

        Set<PermissionsEntity> permissions = permissionsRepository.findAllById(permissionIds)
                .stream()
                .collect(Collectors.toSet());

        if (permissions.size() != permissionIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some permissions not found");
        }
        for (PermissionsEntity permission : permissions) {
            role.addPermission(permission);
        }

        rolesRepository.save(role);

        Set<PermissionsEntity> permissionSet = role.getPermissions().stream()
                .map(RolePermissionEntity::getPermission)
                .collect(Collectors.toSet());

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .status(role.getStatus())
                .permissions(permissionSet)
                .created_by(role.getCreated_by())
                .createdAt(role.getCreatedAt())
                .build();
    }

    public RoleResponse GetRole(Long id) {
        Roles role = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        Set<PermissionsEntity> permissionSet = role.getPermissions().stream()
                .map(RolePermissionEntity::getPermission)
                .collect(Collectors.toSet());

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .status(role.getStatus())
                .permissions(permissionSet) // Use the transformed set
                .created_by(role.getCreated_by())
                .createdAt(role.getCreatedAt())
                .build();
    }

    public List<RoleResponse> getAllRole() {
        List<Roles> roleList = rolesRepository.findAll();

        return roleList.stream().map(
                roles -> RoleResponse.builder()
                        .id(roles.getId())
                        .name(roles.getName())
                        .status(roles.getStatus())
                        .created_by(roles.getCreated_by())
                        .createdAt(roles.getCreatedAt())
                        .build()).collect(Collectors.toList());
    }

    public RoleResponse UpdateRole(Long id, RoleReq request) {
        validationService.validate(request);
        Roles roles = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        roles.setUpdate_by(currentUsername);
        roles.setName(request.getName());
        roles.setStatus(request.getStatus());
        roles.setUpdatedAt(LocalDateTime.now());
        rolesRepository.save(roles);


        return RoleResponse.builder()
                .id(roles.getId())
                .name(roles.getName())
                .status(roles.getStatus())
                .updatedBy(roles.getUpdate_by())
                .updatedAt(roles.getUpdatedAt())
                .build();
    }

    public void DeleteRole(Long id) {
        Roles roles = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        rolePermissionRepository.deleteByRoleId(id);

        rolesRepository.delete(roles);
    }
}