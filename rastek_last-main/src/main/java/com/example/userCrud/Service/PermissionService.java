package com.example.userCrud.Service;

import com.example.userCrud.Dto.PermissionReq;
import com.example.userCrud.Dto.PermissionRes;
import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Repository.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public PermissionRes createPermission(PermissionReq req) {
        validationService.validate(req);

        PermissionsEntity permissions = new PermissionsEntity();
        permissions.setCategory(req.getCategory());
        permissions.setSubCategory(req.getSubCategory());
        permissions.setAction(req.getAction());
        permissions.setDescription(req.getDescription());

        return toPermissionResponse(permissions);
    }

    @Transactional
    public PermissionRes getPermissions(Long id) {
        PermissionsEntity permissions = permissionsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found"));

        return toPermissionResponse(permissions);
    }

    @Transactional(readOnly = true)
    public List<PermissionRes> getAllPermissions() {
        List<PermissionsEntity> permissions = permissionsRepository.findAll();
        return permissions.stream().map(this::toPermissionResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deletePermission(Long id) {
        PermissionsEntity permissions = permissionsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found"));

        permissionsRepository.delete(permissions);
    }

    private PermissionRes toPermissionResponse(PermissionsEntity permissions) {
        return PermissionRes.builder()
                .id(permissions.getId())
                .category(permissions.getCategory())
                .subCategory(permissions.getSubCategory())
                .action(permissions.getAction())
                .description(permissions.getDescription())
                .permissionKey(permissions.getPermissionKey())
                .build();
    }
}
