package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private RolesRepository rolesRepository;

    @Transactional
    public UserResponse create_user(CreateUserRequest request) {

        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        User user = new User();

        if (request.getIdRoles() != null) {
            Roles roles = rolesRepository.findFirstById(request.getIdRoles())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found"));
            user.getRoles().add(roles);
        }

        if (request.getNIK() != null) {
            EmployeeEntity employee = employeeRepository.findFirstByNIK(request.getNIK())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));
            user.setEmployee(employee);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setCreated_by(currentUsername);
        userRepository.save(user);

        List<String> role = user.getRoles().stream()
                .map(Roles::getName)
                .toList();
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .created_at(user.getCreatedAt())
                .updated_at(user.getUpdatedAt())
                .created_by(user.getCreated_by())
                .roles(role)
                .build();
    }

    @Transactional
    public UserResponse addRole(Long id, AddRoleRequest request) {
        validationService.validate(request);

        Roles roles = rolesRepository.findFirstById(request.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        if (user.is_deleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Is Deleted from our World");
        }

        if (Objects.nonNull(request.getRoleId())) {
            user.getRoles().add(roles);
        }

        user.setUpdate_by(currentUsername);

        userRepository.save(user);

        List<String> role = user.getRoles().stream()
                .map(Roles::getName)
                .toList();
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .created_at(user.getCreatedAt())
                .updated_at(user.getUpdatedAt())
                .created_by(user.getCreated_by())
                .updated_by(user.getUpdate_by())
                .roles(role)
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = userRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with ID %d not found", id)
                ));

        if (user.is_deleted()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User has been deleted"
            );
        }

        // Map roles safely
        List<String> roles = Optional.ofNullable(user.getRoles())
                .map(roleList -> roleList.stream()
                        .map(Roles::getName)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .is_active(user.is_active())
                .is_deleted(user.is_deleted())
                .created_at(user.getCreatedAt())
                .updated_at(user.getUpdatedAt())
                .created_by(user.getCreated_by())
                .updated_by(user.getUpdate_by())
                .roles(roles)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUser() {
        List<User> userList = userRepository.findAll();

        return userList.stream().map(
                user -> UserResponse.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .roles(user.getRoles().stream()
                                .map(Roles::getName) // Extract name from each role
                                .collect(Collectors.toList()))
                        .created_at(user.getCreatedAt())
                        .updated_at(user.getUpdatedAt())
                        .created_by(user.getCreated_by())
                        .updated_by(user.getUpdate_by())
                        .build()).collect(Collectors.toList());
    }


    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest userRequest) {

        User user = userRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (user.is_deleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Is Deleted from our World");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (Objects.nonNull(userRequest.getUsername()) && !userRequest.getUsername().isEmpty()) {
            user.setUsername(userRequest.getUsername());
            userRepository.save(user);
        }

        if (Objects.nonNull(userRequest.getEmail()) && !userRequest.getEmail().isEmpty()) {
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
        }

        if (Objects.nonNull(userRequest.getPassword()) && !userRequest.getPassword().isEmpty()) {
            user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);
        }

        // Get the authenticated username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        user.setUpdate_by(currentUsername);

        List<String> roles = user.getRoles().stream()
                .map(Roles::getName)
                .toList();
        //        UserResponse userResponse = toUserResponse(employeeEntity.getUser());


        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .created_at(user.getCreatedAt())
                .created_by(user.getCreated_by())
                .updated_at(user.getUpdatedAt())
                .updated_by(user.getUpdate_by())
                .roles(roles)
                .build();
    }

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        if (user.is_deleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Is Already Deleted from our World");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        user.setUpdate_by(currentUsername);
        user.set_active(false);
        user.set_deleted(true);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Load the user's roles
        List<Roles> roles = user.getRoles();

        // Extract permissions from roles
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .flatMap(role -> role.getPermissions().stream()
                        .map(RolePermissionEntity::getPermission)
                )
                .map(PermissionsEntity::getPermissionKey) // Extract permission key
                .map(SimpleGrantedAuthority::new) // Convert to GrantedAuthority
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}