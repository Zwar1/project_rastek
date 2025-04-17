package com.example.userCrud.Service;

import com.example.userCrud.Dto.UserResponse;
import com.example.userCrud.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.userCrud.Dto.CreateRoleRequest;
import com.example.userCrud.Dto.RoleResponse;
import com.example.userCrud.Entity.Roles;
import com.example.userCrud.Repository.RolesRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public RoleResponse CreateRole(CreateRoleRequest request){
        validationService.validate(request);

        if(rolesRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }

        // Get the authenticated username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Roles role = new Roles();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        role.setCreated_by(currentUsername);

        rolesRepository.save(role);

        return RoleResponse.builder().id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .created_at(role.getCreatedAt())
            .updated_at(role.getUpdatedAt())
            .created_by(role.getCreated_by())
            .build();
    }

    public RoleResponse GetRole(Long id){
        Roles role = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .created_at(role.getCreatedAt())
                .created_by(role.getCreated_by())
                .updated_at(role.getUpdatedAt())
                .updated_by(role.getUpdate_by())
                .build();
    }

    public List<RoleResponse> getAllRole(){
        List<Roles > roleList = rolesRepository.findAll();

        return roleList.stream().map(
                roles -> RoleResponse.builder()
                        .id(roles.getId())
                        .name(roles.getName())
                        .description(roles.getDescription())
                        .created_at(roles.getCreatedAt())
                        .updated_at(roles.getUpdatedAt())
                        .created_by(roles.getCreated_by())
                        .updated_by(roles.getUpdate_by())
                        .build()).collect(Collectors.toList());
    }

    public RoleResponse UpdateRole(Long id, CreateRoleRequest request){
        validationService.validate(request);
        Roles roles = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        // Get the authenticated username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        roles.setUpdate_by(currentUsername);
        roles.setName(request.getName());
        roles.setDescription(request.getDescription());
        rolesRepository.save(roles);


        return RoleResponse.builder()
                .id(roles.getId())
                .name(roles.getName())
                .description(roles.getDescription())
                .created_at(roles.getCreatedAt())
                .created_by(roles.getCreated_by())
                .updated_at(roles.getUpdatedAt())
                .updated_by(roles.getUpdate_by())
                .build();
    }

    public void DeleteRole(Long id){
        Roles roles = rolesRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found"));

        rolesRepository.delete(roles);
    }

    

}
